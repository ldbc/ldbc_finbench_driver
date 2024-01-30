package org.ldbcouncil.finbench.driver.driver;

import static java.lang.String.format;

import com.google.common.base.Charsets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.ldbcouncil.finbench.driver.Db;
import org.ldbcouncil.finbench.driver.DbException;
import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.WorkloadStreams;
import org.ldbcouncil.finbench.driver.control.ControlService;
import org.ldbcouncil.finbench.driver.generator.GeneratorFactory;
import org.ldbcouncil.finbench.driver.generator.RandomDataGeneratorFactory;
import org.ldbcouncil.finbench.driver.log.LoggingService;
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;
import org.ldbcouncil.finbench.driver.runtime.DefaultQueues;
import org.ldbcouncil.finbench.driver.runtime.SimpleResultsLogWriter;
import org.ldbcouncil.finbench.driver.runtime.WorkloadRunner;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeException;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeService;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeServiceAssistant;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeWriter;
import org.ldbcouncil.finbench.driver.runtime.metrics.DisruptorSbeMetricsService;
import org.ldbcouncil.finbench.driver.runtime.metrics.JsonWorkloadMetricsFormatter;
import org.ldbcouncil.finbench.driver.runtime.metrics.MetricsCollectionException;
import org.ldbcouncil.finbench.driver.runtime.metrics.MetricsManager;
import org.ldbcouncil.finbench.driver.runtime.metrics.MetricsService;
import org.ldbcouncil.finbench.driver.runtime.metrics.NullResultsLogWriter;
import org.ldbcouncil.finbench.driver.runtime.metrics.ResultsLogWriter;
import org.ldbcouncil.finbench.driver.runtime.metrics.WorkloadResultsSnapshot;
import org.ldbcouncil.finbench.driver.runtime.metrics.WorkloadStatusSnapshot;
import org.ldbcouncil.finbench.driver.temporal.TemporalUtil;
import org.ldbcouncil.finbench.driver.temporal.TimeSource;
import org.ldbcouncil.finbench.driver.util.ClassLoaderHelper;
import org.ldbcouncil.finbench.driver.util.Tuple3;
import org.ldbcouncil.finbench.driver.validation.ResultsLogValidationResult;
import org.ldbcouncil.finbench.driver.validation.ResultsLogValidationSummary;
import org.ldbcouncil.finbench.driver.validation.ResultsLogValidationTolerances;
import org.ldbcouncil.finbench.driver.validation.ResultsLogValidator;

/**
 * Automatic test mode, using dichotomous method to test the configuration parameters suitable for the current machine
 */
public class AutomaticTestMode implements DriverMode<Object> {
    private final ControlService controlService;
    private final TimeSource timeSource;
    private final LoggingService loggingService;
    private final long randomSeed;
    private final TemporalUtil temporalUtil;
    private final ResultsDirectory resultsDirectory;

    private Workload workload = null;
    private Db database = null;
    private MetricsService metricsService = null;
    private CompletionTimeService completionTimeService = null;
    private WorkloadRunner workloadRunner = null;
    private ResultsLogWriter resultsLogWriter = null;

    public AutomaticTestMode(
        ControlService controlService,
        TimeSource timeSource,
        long randomSeed) throws DriverException {
        this.controlService = controlService;
        this.timeSource = timeSource;
        this.loggingService = controlService.loggingServiceFactory()
            .loggingServiceFor(getClass().getSimpleName());
        this.randomSeed = randomSeed;
        this.temporalUtil = new TemporalUtil();
        this.resultsDirectory = new ResultsDirectory(controlService.configuration());
    }

    public WorkloadStatusSnapshot status() throws MetricsCollectionException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void init() throws DriverException {
        loggingService.info("Driver Configuration");
        loggingService.info(controlService.toString());
    }

    @Override
    public Object startExecutionAndAwaitCompletion() throws DriverException {
        double l = controlService.configuration().tcrMin();
        double r = controlService.configuration().tcrMax();
        // Record the results of the current and last successful one
        ResultsLogValidationResult successfulResult = new ResultsLogValidationResult();
        ResultsLogValidationResult currentResult;

        loggingService.info("--------------------------Rapid estimate phase--------------------------");
        // Assuming that tcr=1, the approximate number of basic operations required in a minute
        // (initially set to be larger, and later change according to the machine situation)
        long baseCnt = 1000;
        // Stores the original total number of operations
        long sourceOpCnt = controlService.configuration().operationCount();
        long sourceWaCnt = controlService.configuration().operationCount();
        // Gets the binary end condition, tolerance range
        double range = controlService.configuration().dichotomyErrorRange();
        // Time compression ratio of this round
        double tcr = 0;
        int numberOfRounds = 1;
        while (r - l >= range) {
            // The first time you try to run with the tcr specified by the configuration
            if (tcr != 0) {
                tcr = l + (r - l) / 2;
                controlService.configuration().setTimeCompressionRatio(tcr);
            } else {
                tcr = controlService.configuration().timeCompressionRatio();
            }
            // Sets the number of preheat operations
            computeRunOperationCount(baseCnt, sourceWaCnt, sourceOpCnt);
            loggingService.info(String.format("--New round %d: Compression ratio: %f,\t warmup count: %d--",
                numberOfRounds++, tcr, controlService.configuration().warmupCount()
            ));
            currentResult = validationTest(true);

            if (currentResult.isSuccessful()) {
                r = tcr;
                successfulResult = currentResult;
            } else {
                l = tcr;
            }
            // Based on the current throughput, determine the base operand per minute at tcr=1
            baseCnt = (long) (Math.ceil(currentResult.throughput()) * tcr * 60);
        }

        loggingService.info("--------------------------Accurate adjust parameter phase--------------------------");
        // The theory is accurate tcr >= estimated tcr,
        // to prevent accidents and give a little bit of room that may be smaller
        l = controlService.configuration().tcrMin() + (l - controlService.configuration().tcrMin()) * 0.7;
        r = Math.min(l * 10, controlService.configuration().tcrMax());
        // Ensure that at least one precision tuning phase is performed
        tcr = 0;
        numberOfRounds = 1;
        do {
            // The first attempt is to run with the results of the estimation phase
            if (tcr != 0) {
                tcr = l + (r - l) / 2;
                controlService.configuration().setTimeCompressionRatio(tcr);
            } else {
                tcr = controlService.configuration().timeCompressionRatio();
            }
            // Set the number of operations for the warm-up and formal phases
            computeRunOperationCount(baseCnt, sourceWaCnt, sourceOpCnt);
            loggingService.info(String.format(
                "--New round %d: Compression ratio: %f,\t warmup count: %d,\t operation count: %d--",
                numberOfRounds++, tcr, controlService.configuration().warmupCount(),
                controlService.configuration().operationCount()
            ));

            currentResult = validationTest(false);
            if (currentResult.isSuccessful()) {
                r = tcr;
                successfulResult = currentResult;
            } else {
                l = tcr;
            }
            baseCnt = (long) (Math.ceil(currentResult.throughput()) * tcr * 60);
        } while (r - l >= range);

        // If finding the last l is not successful, replace it with the last successful one
        if (!currentResult.isSuccessful()) {
            controlService.configuration().setTimeCompressionRatio(r);
            // At this point, the state of the machine has slipped, and there is no need to repeat the test
        }
        try {
            loggingService.info("Shutting down database connector...");
            Instant dbShutdownStart = Instant.now();
            database.close();
            Duration shutdownDuration = Duration.between(dbShutdownStart, Instant.now());
            loggingService.info("Database connector shutdown successfully in: " + shutdownDuration);
        } catch (IOException e) {
            throw new DriverException("Error shutting down database", e);
        }
        loggingService.info("Workload completed successfully");
        loggingService.info(String.format("\n"
                + "--------------------------------------------------------------------------\n"
                + "------- time compression ratio suitable for the machine: %-10f-------\n"
                + "--------------------------------------------------------------------------",
            controlService.configuration().timeCompressionRatio()));
        return successfulResult;
    }

    /**
     * Set the number of operations
     *
     * @param baseCnt Assuming that tcr=1, the approximate number of basic operations required in a minute
     * @param sourceWaCnt Boot specified in the configuration source warmup count
     * @param sourceOpCnt Boot specified in the configuration source operation count
     */
    public void computeRunOperationCount(long baseCnt,
                                         long sourceWaCnt,
                                         long sourceOpCnt) {
        // Enlarge baseCnt by 30 times to prevent accidents and adjust the quantity according to tcr
        double perMinute = baseCnt * 30 / controlService.configuration().timeCompressionRatio();
        if (controlService.configuration().estimateTestTime() != -1) {
            // baseCnt is one minute, now converted to estimate time
            controlService.configuration().setWarmupCount((long) Math.max(
                perMinute * (controlService.configuration().estimateTestTime() / 60000.0), sourceWaCnt
            ));
        }
        if (controlService.configuration().accurateTestTime() != -1) {
            controlService.configuration().setOperationCount((long) Math.max(
                perMinute * (controlService.configuration().accurateTestTime() / 60000.0), sourceOpCnt
            ));
        }
    }

    /**
     * Perform a configuration parameter test
     *
     * @return ResultsLogValidationResult test result
     */
    private ResultsLogValidationResult validationTest(boolean warmup) throws DriverException {
        ResultsLogValidationResult result = executionAndAwaitCompletion(warmup);
        if (result == null) {
            loggingService.info("Obtain result The test result failed");
            return new ResultsLogValidationResult();
        }
        loggingService.info(String.format("\n"
                + "---------------------------- This test check %-8s-------------------\n"
                + "---------------------------- tcr: %-10f----------------------------\n"
                + "---------------------------- throughput: %-10f---------------------\n"
                + "---------------------------- onTimeRatio: %-10f--------------------",
            result.isSuccessful() ? "PASS" : "FAILURE",
            controlService.configuration().timeCompressionRatio(), result.throughput(), result.onTimeRatio()));
        return result;
    }

    public ResultsLogValidationResult executionAndAwaitCompletion(boolean warmup)
        throws DriverException {
        ResultsLogValidationResult result = null;
        if (controlService.configuration().warmupCount() > 0) {
            loggingService.info("\n"
                + " --------------------\n"
                + " --- Warmup Phase ---\n"
                + " --------------------");
            doInit(true);
            result = doExecute(true, controlService.configuration().estimateTestTime());
            try {
                // TODO remove in future
                // This is necessary to clear the runnable context pool
                // As objects in the pool would otherwise hold references to services used during warmup
                loggingService.info("reInit database...");
                database.reInitAutomatic();
            } catch (DbException e) {
                throw new DriverException(format("Error reinitializing DB: %s", database.getClass()
                    .getName()), e);
            }
        } else {
            loggingService.info("\n"
                + " ---------------------------------\n"
                + " --- No Warmup Phase Requested ---\n"
                + " ---------------------------------");
        }

        if (!warmup) {
            loggingService.info("\n"
                + " -----------------\n"
                + " --- Run Phase ---\n"
                + " -----------------");
            doInit(false);
            result = doExecute(false, controlService.configuration().accurateTestTime());
            try {
                // TODO remove in future
                // This is necessary to clear the runnable context pool
                // As objects in the pool would otherwise hold references to services used during warmup
                loggingService.info("reInit database...");
                database.reInitAutomatic();
            } catch (DbException e) {
                throw new DriverException(format("Error reinitializing DB: %s", database.getClass()
                    .getName()), e);
            }
        }
        return result;
    }

    private void doInit(boolean warmup) throws DriverException {
        ConcurrentErrorReporter errorReporter = new ConcurrentErrorReporter();
        GeneratorFactory gf = new GeneratorFactory(new RandomDataGeneratorFactory(randomSeed));

        //  ================================
        //  ===  Results Log CSV Writer  ===
        //  ================================
        File resultsLog = resultsDirectory.getOrCreateResultsLogFile(warmup);
        try {
            resultsLogWriter = (null == resultsLog)
                ? new NullResultsLogWriter()
                : new SimpleResultsLogWriter(
                    resultsLog,
                    controlService.configuration().timeUnit(),
                    controlService.configuration().flushLog());
        } catch (IOException e) {
            throw new DriverException(
                format("Error creating results log writer for: %s", resultsLog.getAbsolutePath()), e);
        }

        //  ------------------
        //  ---  Workload  ---
        //  ------------------
        loggingService.info("Scanning workload streams to calculate their limits...");

        long offset = (warmup)
            ? controlService.configuration().skipCount()
            : controlService.configuration().skipCount() + controlService.configuration().warmupCount();
        long limit = (warmup)
            ? controlService.configuration().warmupCount()
            : controlService.configuration().operationCount();

        WorkloadStreams workloadStreams;
        long minimumTimeStamp;
        try {
            boolean returnStreamsWithDbConnector = true;
            Tuple3<WorkloadStreams, Workload, Long> streamsAndWorkloadAndMinimumTimeStamp =
                WorkloadStreams.createNewWorkloadWithOffsetAndLimitedWorkloadStreams(
                    controlService.configuration(),
                    gf,
                    returnStreamsWithDbConnector,
                    offset,
                    limit,
                    controlService.loggingServiceFactory()
                );
            workloadStreams = streamsAndWorkloadAndMinimumTimeStamp._1();
            workload = streamsAndWorkloadAndMinimumTimeStamp._2();
            minimumTimeStamp = streamsAndWorkloadAndMinimumTimeStamp._3();
        } catch (Exception e) {
            throw new DriverException(format("Error loading workload class: %s",
                controlService.configuration().workloadClassName()), e);
        }
        loggingService.info(format("Loaded workload: %s", workload.getClass().getName()));

        loggingService.info(format("Retrieving workload stream: %s", workload.getClass().getSimpleName()));
        controlService.setWorkloadStartTimeAsMilli(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5));
        WorkloadStreams timeMappedWorkloadStreams;
        try {
            timeMappedWorkloadStreams = WorkloadStreams.timeOffsetAndCompressWorkloadStreams(
                workloadStreams,
                controlService.workloadStartTimeAsMilli(),
                controlService.configuration().timeCompressionRatio(),
                gf
            );
        } catch (WorkloadException e) {
            throw new DriverException("Error while retrieving operation stream for workload", e);
        }

        //  ---------------=
        //  ---==  DB  ---==
        //  ---------------=
        if (null == database) {
            try {
                database = ClassLoaderHelper.loadDb(controlService.configuration().dbClassName());
                database.init(
                    controlService.configuration().asMap(),
                    controlService.loggingServiceFactory().loggingServiceFor(database.getClass().getSimpleName()),
                    workload.operationTypeToClassMapping()
                );
            } catch (DbException e) {
                throw new DriverException(
                    format("Error initializing DB: %s", controlService.configuration().dbClassName()), e);
            }
            loggingService.info(format("Loaded DB: %s", database.getClass().getName()));
        }

        //  ------------------------
        //  ---  Metrics Service  ==
        //  ------------------------
        try {
            // TODO create metrics service factory so different ones can be easily created
            metricsService = new DisruptorSbeMetricsService(
                timeSource,
                errorReporter,
                controlService.configuration().timeUnit(),
                DisruptorSbeMetricsService.DEFAULT_HIGHEST_EXPECTED_RUNTIME_DURATION_AS_NANO,
                resultsLogWriter,
                workload.operationTypeToClassMapping(),
                controlService.loggingServiceFactory()
            );
        } catch (MetricsCollectionException e) {
            throw new DriverException("Error creating metrics service", e);
        }

        //  ---------------------------------
        //  ---  Completion Time Service  ---
        //  ---------------------------------
        CompletionTimeServiceAssistant completionTimeServiceAssistant = new CompletionTimeServiceAssistant();
        try {
            completionTimeService =
                completionTimeServiceAssistant.newThreadedQueuedCompletionTimeService(
                    timeSource,
                    errorReporter
                );
        } catch (CompletionTimeException e) {
            throw new DriverException("Error instantiating Completion Time Service", e);
        }

        //  ------------------------
        //  ---  Workload Runner  ==
        //  ------------------------
        loggingService.info(format("Instantiating %s", WorkloadRunner.class.getSimpleName()));
        try {
            int operationHandlerExecutorsBoundedQueueSize = DefaultQueues.DEFAULT_BOUND_1000;
            workloadRunner = new WorkloadRunner(
                timeSource,
                database,
                timeMappedWorkloadStreams,
                metricsService,
                errorReporter,
                completionTimeService,
                controlService.loggingServiceFactory(),
                controlService.configuration().threadCount(),
                controlService.configuration().statusDisplayIntervalAsSeconds(),
                controlService.configuration().spinnerSleepDurationAsMilli(),
                controlService.configuration().ignoreScheduledStartTimes(),
                operationHandlerExecutorsBoundedQueueSize);
        } catch (Exception e) {
            throw new DriverException(format("Error instantiating %s", WorkloadRunner.class.getSimpleName()), e);
        }

        //  ------------------------------------------=
        //  ---  Initialize Completion Time Service  ==
        //  ------------------------------------------=
        // TODO note, this MUST be done after creation of Workload Runner because Workload Runner creates the
        // TODO "writers" for completion time service (refactor this mess at some stage)
        try {
            if (completionTimeService.getAllWriters().isEmpty()) {
                // There are no completion time writers, CT would never advance or be non-null,
                // set to max so nothing ever waits on it
                long nearlyMaxPossibleTimeAsMilli = Long.MAX_VALUE - 1;
                long maxPossibleTimeAsMilli = Long.MAX_VALUE;
                // Create a writer to use for advancing CT
                CompletionTimeWriter completionTimeWriter = completionTimeService.newCompletionTimeWriter();
                completionTimeWriter.submitInitiatedTime(nearlyMaxPossibleTimeAsMilli);
                completionTimeWriter.submitCompletedTime(nearlyMaxPossibleTimeAsMilli);
                completionTimeWriter.submitInitiatedTime(maxPossibleTimeAsMilli);
                completionTimeWriter.submitCompletedTime(maxPossibleTimeAsMilli);
            } else {
                // There are some completion time writers, initialize them to lowest time stamp in workload
                completionTimeServiceAssistant.writeInitiatedAndCompletedTimesToAllWriters(
                    completionTimeService, minimumTimeStamp - 1);
                completionTimeServiceAssistant
                    .writeInitiatedAndCompletedTimesToAllWriters(completionTimeService, minimumTimeStamp);
                boolean completionTimeAdvancedToDesiredTime =
                    completionTimeServiceAssistant.waitForCompletionTime(
                        timeSource,
                        minimumTimeStamp - 1,
                        TimeUnit.SECONDS.toMillis(5),
                        completionTimeService,
                        errorReporter
                    );
                long completionTimeWaitTimeoutDurationAsMilli = TimeUnit.SECONDS.toMillis(5);
                if (!completionTimeAdvancedToDesiredTime) {
                    throw new DriverException(
                        format(
                            "Timed out [%s] while waiting for completion time to advance to workload "
                                + "start time\nCurrent CT: %s\nWaiting For CT: %s",
                            completionTimeWaitTimeoutDurationAsMilli,
                            completionTimeService.completionTimeAsMilli(),
                            controlService.workloadStartTimeAsMilli())
                    );
                }
                loggingService.info("CT: " + temporalUtil
                    .milliTimeToDateTimeString(completionTimeService.completionTimeAsMilli()) + " / "
                    + completionTimeService.completionTimeAsMilli());
            }
        } catch (CompletionTimeException e) {
            throw new DriverException(
                "Error while writing initial initiated and completed times to Completion Time Service", e);
        }
    }

    private ResultsLogValidationResult doExecute(boolean warmup,
                                                 long milli) throws DriverException {
        try {
            ConcurrentErrorReporter errorReporter = null;
            if (milli == -1) {
                // To execute normally, follow the EXECUTE_BENCHMARK process
                errorReporter = workloadRunner.getFuture().get();
            } else {
                errorReporter = workloadRunner.getFuture(milli);
            }
            loggingService.info("Shutting down workload...");
            workload.close();
            if (errorReporter.errorEncountered()) {
                throw new DriverException("Error running workload\n" + errorReporter.toString());
            }
        } catch (Exception e) {
            throw new DriverException("Error running workload", e);
        }

        loggingService.info("Shutting down completion time service...");
        try {
            completionTimeService.shutdown();
        } catch (CompletionTimeException e) {
            throw new DriverException("Error during shutdown of completion time service", e);
        }

        loggingService.info("Shutting down metrics collection service...");
        WorkloadResultsSnapshot workloadResults;
        try {
            workloadResults = metricsService.getWriter().results();
            metricsService.shutdown();
        } catch (MetricsCollectionException e) {
            throw new DriverException("Error during shutdown of metrics collection service", e);
        }

        try {
            if (warmup) {
                loggingService.summaryResult(workloadResults);
            } else {
                loggingService.detailedResult(workloadResults);
            }
            if (resultsDirectory.exists()) {
                File resultsSummaryFile = resultsDirectory.getOrCreateResultsSummaryFile(warmup);
                loggingService.info(
                    format("Exporting workload metrics to %s...", resultsSummaryFile.getAbsolutePath())
                );
                MetricsManager.export(workloadResults,
                    new JsonWorkloadMetricsFormatter(),
                    new FileOutputStream(resultsSummaryFile),
                    Charsets.UTF_8
                );
                File configurationFile = resultsDirectory.getOrCreateConfigurationFile(warmup);
                Files.write(
                    configurationFile.toPath(),
                    controlService.configuration().toPropertiesString().getBytes(StandardCharsets.UTF_8)
                );
                resultsLogWriter.close();
                if (!controlService.configuration().ignoreScheduledStartTimes()) {
                    loggingService.info("Validating workload results...");
                    // TODO make this feature accessible directly
                    ResultsLogValidator resultsLogValidator = new ResultsLogValidator();
                    ResultsLogValidationTolerances resultsLogValidationTolerances =
                        workload.resultsLogValidationTolerancesAutomatic(controlService.configuration(),
                            workloadResults.totalOperationCount());

                    ResultsLogValidationSummary resultsLogValidationSummary = resultsLogValidator.compute(
                        resultsDirectory.getOrCreateResultsLogFile(warmup),
                        resultsLogValidationTolerances.excessiveDelayThresholdAsMilli()
                    );
                    File resultsValidationFile = resultsDirectory.getOrCreateResultsValidationFile(warmup);
                    loggingService.info(
                        format("Exporting workload results validation to: %s",
                            resultsValidationFile.getAbsolutePath())
                    );
                    // Files.write(
                    //        resultsValidationFile.toPath(),
                    //        resultsLogValidationSummary.toJson().getBytes(StandardCharsets.UTF_8)
                    // );
                    // TODO export result
                    ResultsLogValidationResult validationResult = resultsLogValidator.validateAutomatic(
                        resultsLogValidationSummary,
                        resultsLogValidationTolerances,
                        workloadResults
                    );
                    loggingService.info(validationResult.getScheduleAuditResult(
                        controlService.configuration().recordDelayedOperations()
                    ));
                    Files.write(
                        resultsValidationFile.toPath(),
                        resultsLogValidationSummary.toJson().getBytes(StandardCharsets.UTF_8)
                    );
                    return validationResult;
                }
            }
        } catch (Exception e) {
            throw new DriverException("Could not export workload metrics", e);
        }
        return null;
    }
}

