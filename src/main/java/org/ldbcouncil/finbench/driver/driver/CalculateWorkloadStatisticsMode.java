package org.ldbcouncil.finbench.driver.driver;

import static java.lang.String.format;

import java.util.concurrent.TimeUnit;
import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.WorkloadStreams;
import org.ldbcouncil.finbench.driver.control.ControlService;
import org.ldbcouncil.finbench.driver.generator.GeneratorFactory;
import org.ldbcouncil.finbench.driver.generator.RandomDataGeneratorFactory;
import org.ldbcouncil.finbench.driver.log.LoggingService;
import org.ldbcouncil.finbench.driver.runtime.metrics.MetricsCollectionException;
import org.ldbcouncil.finbench.driver.statistics.WorkloadStatistics;
import org.ldbcouncil.finbench.driver.statistics.WorkloadStatisticsCalculator;
import org.ldbcouncil.finbench.driver.util.Tuple3;

public class CalculateWorkloadStatisticsMode implements DriverMode<WorkloadStatistics> {
    private final ControlService controlService;
    private final LoggingService loggingService;
    private final long randomSeed;

    private Workload workload = null;
    private WorkloadStreams timeMappedWorkloadStreams = null;

    public CalculateWorkloadStatisticsMode(ControlService controlService, long randomSeed) throws DriverException {
        this.controlService = controlService;
        this.loggingService = controlService.loggingServiceFactory().loggingServiceFor(getClass().getSimpleName());
        this.randomSeed = randomSeed;
    }

    @Override
    public void init() throws DriverException {
        GeneratorFactory gf = new GeneratorFactory(new RandomDataGeneratorFactory(randomSeed));
        WorkloadStreams workloadStreams;
        try {
            boolean returnStreamsWithDbConnector = false;
            Tuple3<WorkloadStreams, Workload, Long> workloadStreamsAndWorkload =
                WorkloadStreams.createNewWorkloadWithOffsetAndLimitedWorkloadStreams(
                    controlService.configuration(),
                    gf,
                    returnStreamsWithDbConnector,
                    0,
                    controlService.configuration().operationCount(),
                    controlService.loggingServiceFactory()
                );
            workloadStreams = workloadStreamsAndWorkload._1();
            workload = workloadStreamsAndWorkload._2();
        } catch (Exception e) {
            throw new DriverException(format("Error loading Workload class: %s",
                controlService.configuration().workloadClassName()), e);
        }
        loggingService.info(format("Loaded Workload: %s", workload.getClass().getName()));

        loggingService.info(
            format("Retrieving operation stream for workload: %s", workload.getClass().getSimpleName()));
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

        loggingService.info("Driver Configuration");
        loggingService.info(controlService.toString());
    }

    @Override
    public WorkloadStatistics startExecutionAndAwaitCompletion() throws DriverException {
        loggingService.info(
            format("Calculating workload statistics for: %s", workload.getClass().getSimpleName()));
        WorkloadStatistics workloadStatistics;
        try {
            WorkloadStatisticsCalculator workloadStatisticsCalculator = new WorkloadStatisticsCalculator();
            workloadStatistics = workloadStatisticsCalculator.calculate(
                timeMappedWorkloadStreams,
                TimeUnit.HOURS.toMillis(5)
            // TODO uncomment, maybe
            // workload.maxExpectedInterleave()
            );
            loggingService.info("Calculation complete\n" + workloadStatistics);
        } catch (MetricsCollectionException e) {
            throw new DriverException("Error while calculating workload statistics", e);
        }
        return null;
    }
}
