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

public class ExecuteWorkloadMode implements DriverMode<Object> {
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

    public ExecuteWorkloadMode(
        ControlService controlService,
        TimeSource timeSource,
        long randomSeed) throws DriverException {
        this.controlService = controlService;
        this.timeSource = timeSource;
        this.loggingService = controlService.loggingServiceFactory().loggingServiceFor(getClass().getSimpleName());
        this.randomSeed = randomSeed;
        this.temporalUtil = new TemporalUtil();
        this.resultsDirectory = new ResultsDirectory(controlService.configuration());
    }

    /*
    TODO clientMode.init()
    TODO clientMode
     */
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
        if (controlService.configuration().warmupCount() > 0) {
            loggingService.info("\n"
                + " --------------------\n"
                + " --- Warmup Phase ---\n"
                + " --------------------");
            doInit(true);
            doExecute(true);
            try {
                // TODO remove in future
                // This is necessary to clear the runnable context pool
                // As objects in the pool would otherwise hold references to services used during warmup
                database.reInit();
            } catch (DbException e) {
                throw new DriverException(format("Error reinitializing DB: %s", database.getClass().getName()), e);
            }
        } else {
            loggingService.info("\n"
                + " ---------------------------------\n"
                + " --- No Warmup Phase Requested ---\n"
                + " ---------------------------------");
        }

        loggingService.info("\n"
            + " -----------------\n"
            + " --- Run Phase ---\n"
            + " -----------------");
        doInit(false);
        doExecute(false);

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
        return null;
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

        //  ==================
        //  ===  Workload  ===
        //  ==================
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

        //  ================
        //  =====  DB  =====
        //  ================
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

        //  ========================
        //  ===  Metrics Service  ==
        //  ========================
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

        //  =================================
        //  ===  Completion Time Service  ===
        //  =================================
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

        //  ========================
        //  ===  Workload Runner  ==
        //  ========================
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

        //  ===========================================
        //  ===  Initialize Completion Time Service  ==
        //  ===========================================
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
                completionTimeServiceAssistant
                    .writeInitiatedAndCompletedTimesToAllWriters(completionTimeService, minimumTimeStamp - 1);
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

    private void doExecute(boolean warmup) throws DriverException {
        try {
            ConcurrentErrorReporter errorReporter = workloadRunner.getFuture().get();
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
                        workload.resultsLogValidationTolerances(controlService.configuration(), warmup);


                    ResultsLogValidationSummary resultsLogValidationSummary = resultsLogValidator.compute(
                        resultsDirectory.getOrCreateResultsLogFile(warmup),
                        resultsLogValidationTolerances.excessiveDelayThresholdAsMilli()
                    );
                    File resultsValidationFile = resultsDirectory.getOrCreateResultsValidationFile(warmup);
                    loggingService.info(
                        format("Exporting workload results validation to: %s",
                            resultsValidationFile.getAbsolutePath())
                    );
                    Files.write(
                        resultsValidationFile.toPath(),
                        resultsLogValidationSummary.toJson().getBytes(StandardCharsets.UTF_8)
                    );
                    // TODO export result
                    ResultsLogValidationResult validationResult = resultsLogValidator.validate(
                        resultsLogValidationSummary,
                        resultsLogValidationTolerances,
                        controlService.configuration().recordDelayedOperations(),
                        workloadResults
                    );
                    loggingService.info(validationResult.getScheduleAuditResult(
                        controlService.configuration().recordDelayedOperations()
                    ));
                    Files.write(
                        resultsValidationFile.toPath(),
                        resultsLogValidationSummary.toJson().getBytes(StandardCharsets.UTF_8)
                    );
                }
            }
        } catch (Exception e) {
            throw new DriverException("Could not export workload metrics", e);
        }
    }
}
