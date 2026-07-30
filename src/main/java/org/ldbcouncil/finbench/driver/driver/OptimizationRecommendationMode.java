/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.driver;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.ldbcouncil.finbench.driver.Db;
import org.ldbcouncil.finbench.driver.DbException;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.OperationHandlerRunnableContext;
import org.ldbcouncil.finbench.driver.ResultReporter;
import org.ldbcouncil.finbench.driver.control.ControlService;
import org.ldbcouncil.finbench.driver.control.DriverConfiguration;
import org.ldbcouncil.finbench.driver.control.DriverConfigurationException;
import org.ldbcouncil.finbench.driver.control.LocalControlService;
import org.ldbcouncil.finbench.driver.optimization.OperationResourceProfile;
import org.ldbcouncil.finbench.driver.optimization.OptimizationAnalyzer;
import org.ldbcouncil.finbench.driver.optimization.OptimizationCandidate;
import org.ldbcouncil.finbench.driver.optimization.OptimizationReport;
import org.ldbcouncil.finbench.driver.optimization.OptimizationScenarioResult;
import org.ldbcouncil.finbench.driver.optimization.OptimizationSimulationConfig;
import org.ldbcouncil.finbench.driver.optimization.OptimizationSupport;
import org.ldbcouncil.finbench.driver.optimization.RepresentativeOperationRecorder;
import org.ldbcouncil.finbench.driver.optimization.ResourceSnapshot;
import org.ldbcouncil.finbench.driver.optimization.SyntheticExecutionResult;
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;
import org.ldbcouncil.finbench.driver.runtime.metrics.WorkloadResultsSnapshot;
import org.ldbcouncil.finbench.driver.temporal.SystemTimeSource;
import org.ldbcouncil.finbench.driver.util.ClassLoaderHelper;

public class OptimizationRecommendationMode implements DriverMode<OptimizationReport> {
    public static final String TOP_N = "optimization.top_n";
    public static final String REDUCTION_STEPS = "optimization.reduction_steps";
    public static final String REPETITIONS = "optimization.repetitions";
    public static final String PROFILE_REPETITIONS = "optimization.profile_repetitions";
    public static final String SAMPLE_INTERVAL_MILLIS = "optimization.sample_interval_millis";

    private final ControlService controlService;
    private final long randomSeed;
    private final ResultsDirectory resultsDirectory;

    public OptimizationRecommendationMode(
        ControlService controlService,
        long randomSeed) throws DriverException {
        this.controlService = controlService;
        this.randomSeed = randomSeed;
        this.resultsDirectory = new ResultsDirectory(controlService.configuration());
    }

    @Override
    public void init() throws DriverException {
        if (!resultsDirectory.exists()) {
            throw new DriverException("OPTIMIZATION_RECOMMENDATION requires a results_dir");
        }
    }

    @Override
    public OptimizationReport startExecutionAndAwaitCompletion() throws DriverException {
        RepresentativeOperationRecorder operationRecorder =
            new RepresentativeOperationRecorder();
        ExecuteWorkloadMode baseline =
            new ExecuteWorkloadMode(
                controlService,
                new SystemTimeSource(),
                randomSeed,
                operationRecorder::record);
        baseline.init();
        baseline.startExecutionAndAwaitCompletion();

        WorkloadResultsSnapshot baselineResults = readBaselineResults();
        List<OptimizationCandidate> candidates = new OptimizationAnalyzer().rank(baselineResults);
        List<OperationResourceProfile> profiles = new ArrayList<>();
        List<OptimizationScenarioResult> scenarios = new ArrayList<>();
        boolean simulationSupported =
            executeScenarios(
                baselineResults,
                candidates,
                profiles,
                scenarios,
                operationRecorder);
        OptimizationReport report =
            new OptimizationReport(
                baselineResults.throughput(),
                candidates,
                profiles,
                scenarios,
                simulationSupported);
        writeReport(report);
        return report;
    }

    private WorkloadResultsSnapshot readBaselineResults() throws DriverException {
        try {
            return WorkloadResultsSnapshot.fromJson(
                resultsDirectory.getOrCreateResultsSummaryFile(false));
        } catch (IOException e) {
            throw new DriverException("Unable to read baseline results", e);
        }
    }

    private boolean executeScenarios(
        WorkloadResultsSnapshot baseline,
        List<OptimizationCandidate> candidates,
        List<OperationResourceProfile> profiles,
        List<OptimizationScenarioResult> scenarios,
        RepresentativeOperationRecorder operationRecorder) throws DriverException {
        Db database = null;
        try {
            database = ClassLoaderHelper.loadDb(controlService.configuration().dbClassName());
            database.init(
                controlService.configuration().asMap(),
                controlService.loggingServiceFactory()
                    .loggingServiceFor(database.getClass().getSimpleName()),
                operationRecorder.operationTypeToClassMapping()
            );
            Optional<OptimizationSupport> optionalSupport = database.optimizationSupport();
            if (!optionalSupport.isPresent()) {
                return false;
            }
            runScenarios(
                database,
                optionalSupport.get(),
                baseline,
                candidates,
                profiles,
                scenarios,
                operationRecorder);
            return true;
        } catch (DbException e) {
            throw new DriverException("Unable to initialize optimization support", e);
        } finally {
            if (database != null) {
                try {
                    database.close();
                } catch (IOException e) {
                    throw new DriverException("Unable to close optimization support", e);
                }
            }
        }
    }

    private void runScenarios(
        Db database,
        OptimizationSupport support,
        WorkloadResultsSnapshot baseline,
        List<OptimizationCandidate> candidates,
        List<OperationResourceProfile> profiles,
        List<OptimizationScenarioResult> scenarios,
        RepresentativeOperationRecorder operationRecorder) throws DbException, DriverException {
        Map<String, String> params = controlService.configuration().asMap();
        int topN = integerParam(params, TOP_N, 3);
        int repetitions = integerParam(params, REPETITIONS, 3);
        int profileRepetitions = integerParam(params, PROFILE_REPETITIONS, repetitions);
        int sampleIntervalMillis = integerParam(params, SAMPLE_INTERVAL_MILLIS, 50);
        if (topN <= 0 || repetitions <= 0 || profileRepetitions <= 0 || sampleIntervalMillis <= 0) {
            throw new DbException(
                "Optimization top_n, repetitions, profile_repetitions and sample_interval_millis"
                    + " must be positive"
            );
        }
        List<Double> reductions = reductionSteps(params);

        for (OptimizationCandidate candidate :
            candidates.subList(0, Math.min(topN, candidates.size()))) {
            Operation representativeOperation = operationRecorder.operation(candidate.operation())
                .orElseThrow(() -> new DbException(
                    "No representative operation captured for: " + candidate.operation()));
            OperationResourceProfile profile =
                profileOperation(
                    support,
                    candidate,
                    profileRepetitions,
                    sampleIntervalMillis,
                    () -> executeRepresentativeOperation(database, representativeOperation));
            profiles.add(profile);
            for (double reduction : reductions) {
                long targetDurationNanos = Math.max(
                    1,
                    Math.round(
                        candidate.meanDurationMillis()
                            * TimeUnit.MILLISECONDS.toNanos(1)
                            * (1 - reduction)));
                double totalThroughput = 0;
                String scenarioResultsDirectory =
                    scenarioDirectory(candidate.operation(), reduction).getAbsolutePath();
                for (int repetition = 0; repetition < repetitions; repetition++) {
                    ScenarioExecution scenarioExecution =
                        executeScenarioWorkload(
                            candidate, profile, reduction, targetDurationNanos, repetition);
                    totalThroughput += scenarioExecution.results.throughput();
                }
                double measuredThroughput = totalThroughput / repetitions;
                double measuredGain =
                    baseline.throughput() == 0 ? 0 : measuredThroughput / baseline.throughput() - 1;
                scenarios.add(new OptimizationScenarioResult(
                    candidate.operation(),
                    reduction,
                    measuredThroughput,
                    measuredGain,
                    targetDurationNanos,
                    scenarioResultsDirectory,
                    profile.averageCpuCores(),
                    profile.averageMemoryBytes(),
                    profile.peakMemoryBytes()
                ));
            }
        }
    }

    private ScenarioExecution executeScenarioWorkload(
        OptimizationCandidate candidate,
        OperationResourceProfile profile,
        double reduction,
        long targetDurationNanos,
        int repetition) throws DriverException {
        File scenarioDirectory =
            new File(scenarioDirectory(candidate.operation(), reduction), "run-" + repetition);
        Map<String, String> overrides = new HashMap<>();
        overrides.put("mode", "EXECUTE_BENCHMARK");
        overrides.put("results_dir", scenarioDirectory.getAbsolutePath());
        overrides.put(OptimizationSimulationConfig.ENABLED, "true");
        overrides.put(OptimizationSimulationConfig.OPERATION, candidate.operation());
        overrides.put(
            OptimizationSimulationConfig.TARGET_CPU_CORES,
            Double.toString(profile.averageCpuCores()));
        overrides.put(
            OptimizationSimulationConfig.TARGET_MEMORY_BYTES,
            Long.toString(Math.round(profile.averageMemoryBytes()))
        );
        overrides.put(
            OptimizationSimulationConfig.TARGET_DURATION_NANOS,
            Long.toString(targetDurationNanos));

        try {
            DriverConfiguration scenarioConfiguration =
                controlService.configuration().applyArgs(overrides);
            ControlService scenarioControlService = new LocalControlService(
                controlService.timeSource().nowAsMilli() + TimeUnit.SECONDS.toMillis(5),
                scenarioConfiguration,
                controlService.loggingServiceFactory(),
                controlService.timeSource()
            );
            ExecuteWorkloadMode scenarioMode =
                new ExecuteWorkloadMode(scenarioControlService, new SystemTimeSource(), randomSeed);
            scenarioMode.init();
            scenarioMode.startExecutionAndAwaitCompletion();
            ResultsDirectory resultsDirectory = new ResultsDirectory(scenarioConfiguration);
            WorkloadResultsSnapshot scenarioResults = WorkloadResultsSnapshot.fromJson(
                resultsDirectory.getOrCreateResultsSummaryFile(false)
            );
            scenarioControlService.shutdown();
            return new ScenarioExecution(scenarioResults, scenarioDirectory.getAbsolutePath());
        } catch (DriverConfigurationException e) {
            throw new DriverException("Unable to build optimization scenario configuration", e);
        } catch (IOException e) {
            throw new DriverException("Unable to read optimization scenario results", e);
        }
    }

    private File scenarioDirectory(String operation, double reduction) throws DriverException {
        File scenarioDirectory = new File(
            controlService.configuration().resultDirPath(),
            "optimization-scenarios/" + operation + "/reduction-" + Math.round(reduction * 100)
        );
        if (!scenarioDirectory.exists() && !scenarioDirectory.mkdirs()) {
            throw new DriverException(
                "Unable to create optimization scenario directory: " + scenarioDirectory);
        }
        return scenarioDirectory;
    }

    static OperationResourceProfile profileOperation(
        OptimizationSupport support,
        OptimizationCandidate candidate,
        int repetitions,
        int sampleIntervalMillis,
        RepresentativeOperationCallable representativeOperation) throws DbException {
        long totalDuration = 0;
        long sampleCount = 0;
        double totalCpuCores = 0;
        double totalMemoryBytes = 0;
        long peakMemoryBytes = 0;
        for (int repetition = 0; repetition < repetitions; repetition++) {
            SyntheticRun profileRun = executeAndSample(
                support,
                representativeOperation::execute,
                sampleIntervalMillis
            );
            totalDuration += profileRun.result.actualDurationNanos();
            long runPeakMemoryBytes = profileRun.result.peakMemoryBytes();
            for (ResourceSnapshot sample : profileRun.samples) {
                sampleCount++;
                totalCpuCores += Math.max(
                    0,
                    sample.cpuCores() - profileRun.baseline.cpuCores());
                long memoryDelta = Math.max(
                    0,
                    sample.memoryBytes() - profileRun.baseline.memoryBytes());
                runPeakMemoryBytes = Math.max(runPeakMemoryBytes, memoryDelta);
            }
            totalMemoryBytes += runPeakMemoryBytes;
            peakMemoryBytes = Math.max(peakMemoryBytes, runPeakMemoryBytes);
        }
        return new OperationResourceProfile(
            candidate.operation(),
            totalDuration
                / (double) repetitions
                / TimeUnit.MILLISECONDS.toNanos(1),
            sampleCount == 0 ? 0 : totalCpuCores / sampleCount,
            totalMemoryBytes / repetitions,
            peakMemoryBytes,
            sampleCount
        );
    }

    private static SyntheticExecutionResult executeRepresentativeOperation(
        Db database,
        Operation representativeOperation) throws DbException {
        Operation operation = representativeOperation.newInstance();
        OperationHandlerRunnableContext handlerContext =
            database.getOperationHandlerRunnableContext(operation);
        ResultReporter.SimpleResultReporter resultReporter =
            new ResultReporter.SimpleResultReporter(
                new ConcurrentErrorReporter());
        long start = System.nanoTime();
        try {
            handlerContext.operationHandler().executeOperation(
                operation,
                handlerContext.dbConnectionState(),
                resultReporter);
            if (resultReporter.result() == null) {
                throw new DbException(
                    "Representative operation returned no result: "
                        + operation.getClass().getSimpleName());
            }
        } finally {
            handlerContext.cleanup();
        }
        long durationNanos = System.nanoTime() - start;
        return new SyntheticExecutionResult(durationNanos, 0);
    }

    private static SyntheticRun executeAndSample(
        OptimizationSupport support,
        SyntheticCallable callable,
        int sampleIntervalMillis) throws DbException {
        ResourceSnapshot baseline = sampleResources(support);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        List<ResourceSnapshot> samples = new ArrayList<>();
        Future<SyntheticExecutionResult> future = executor.submit(callable::call);
        try {
            while (!future.isDone()) {
                samples.add(sampleResources(support));
                TimeUnit.MILLISECONDS.sleep(sampleIntervalMillis);
            }
            samples.add(sampleResources(support));
            return new SyntheticRun(future.get(), baseline, samples);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            future.cancel(true);
            throw new DbException("Interrupted while sampling synthetic execution", e);
        } catch (ExecutionException e) {
            throw new DbException("Synthetic execution failed", e.getCause());
        } finally {
            executor.shutdownNow();
        }
    }

    private static ResourceSnapshot sampleResources(OptimizationSupport support)
        throws DbException {
        return new ResourceSnapshot(
            System.currentTimeMillis(),
            support.getCurrentCpuCores(),
            support.getCurrentMemoryBytes());
    }

    private void writeReport(OptimizationReport report) throws DriverException {
        File directory = new File(controlService.configuration().resultDirPath());
        try {
            Files.write(
                new File(directory, "optimization-report.json").toPath(),
                report.toJson().getBytes(StandardCharsets.UTF_8)
            );
            Files.write(
                new File(directory, "optimization-report.md").toPath(),
                report.toMarkdown().getBytes(StandardCharsets.UTF_8)
            );
        } catch (IOException e) {
            throw new DriverException("Unable to write optimization report", e);
        }
    }

    private static List<Double> reductionSteps(Map<String, String> params) throws DbException {
        String configured = params.containsKey(REDUCTION_STEPS)
            ? params.get(REDUCTION_STEPS)
            : "0.05,0.10,0.20,0.30";
        List<Double> values = new ArrayList<>();
        try {
            for (String value : configured.split(",")) {
                double parsed = Double.parseDouble(value.trim());
                if (parsed <= 0 || parsed >= 1) {
                    throw new IllegalArgumentException();
                }
                values.add(parsed);
            }
            return values;
        } catch (IllegalArgumentException e) {
            throw new DbException(format("Invalid %s: %s", REDUCTION_STEPS, configured), e);
        }
    }

    private static int integerParam(Map<String, String> params, String key, int defaultValue) {
        return params.containsKey(key) ? Integer.parseInt(params.get(key)) : defaultValue;
    }

    private interface SyntheticCallable {
        SyntheticExecutionResult call() throws DbException;
    }

    @FunctionalInterface
    interface RepresentativeOperationCallable {
        SyntheticExecutionResult execute() throws DbException;
    }

    private static class SyntheticRun {
        private final SyntheticExecutionResult result;
        private final ResourceSnapshot baseline;
        private final List<ResourceSnapshot> samples;

        SyntheticRun(
            SyntheticExecutionResult result,
            ResourceSnapshot baseline,
            List<ResourceSnapshot> samples) {
            this.result = result;
            this.baseline = baseline;
            this.samples = samples;
        }
    }

    private static class ScenarioExecution {
        private final WorkloadResultsSnapshot results;
        private final String resultsDirectory;

        ScenarioExecution(WorkloadResultsSnapshot results, String resultsDirectory) {
            this.results = results;
            this.resultsDirectory = resultsDirectory;
        }
    }
}
