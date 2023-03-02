package org.ldbcouncil.finbench.driver.runtime;

import static java.lang.String.format;

import java.util.concurrent.atomic.AtomicBoolean;
import org.ldbcouncil.finbench.driver.log.LoggingService;
import org.ldbcouncil.finbench.driver.log.LoggingServiceFactory;
import org.ldbcouncil.finbench.driver.log.RecentThroughputAndDuration;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeService;
import org.ldbcouncil.finbench.driver.runtime.metrics.MetricsService;
import org.ldbcouncil.finbench.driver.runtime.metrics.WorkloadStatusSnapshot;
import org.ldbcouncil.finbench.driver.runtime.scheduling.Spinner;

class WorkloadStatusThread extends Thread {
    private final long statusUpdateIntervalAsMilli;
    private final MetricsService.MetricsServiceWriter metricsServiceWriter;
    private final ConcurrentErrorReporter errorReporter;
    private final CompletionTimeService completionTimeService;
    private final LoggingService loggingService;
    private final AtomicBoolean continueRunning = new AtomicBoolean(true);

    WorkloadStatusThread(
        long statusUpdateIntervalAsMilli,
        MetricsService.MetricsServiceWriter metricsServiceWriter,
        ConcurrentErrorReporter errorReporter,
        CompletionTimeService completionTimeService,
        LoggingServiceFactory loggingServiceFactory) {
        super(WorkloadStatusThread.class.getSimpleName() + "-" + System.currentTimeMillis());
        this.statusUpdateIntervalAsMilli = statusUpdateIntervalAsMilli;
        this.metricsServiceWriter = metricsServiceWriter;
        this.errorReporter = errorReporter;
        this.completionTimeService = completionTimeService;
        this.loggingService = loggingServiceFactory.loggingServiceFor(getClass().getSimpleName());
    }

    @Override
    public void run() {
        final SettableRecentThroughputAndDuration
            settableRecentThroughputAndDuration = new SettableRecentThroughputAndDuration();
        final int statusRecency = 4;
        final long[][] operationCountsAtDurations = new long[statusRecency][2];
        for (int i = 0; i < operationCountsAtDurations.length; i++) {
            operationCountsAtDurations[i][0] = -1;
            operationCountsAtDurations[i][1] = -1;
        }
        int statusRecencyIndex = 0;

        while (continueRunning.get()) {
            try {
                WorkloadStatusSnapshot status = metricsServiceWriter.status();
                operationCountsAtDurations[statusRecencyIndex][0] = status.operationCount();
                operationCountsAtDurations[statusRecencyIndex][1] = status.runDurationAsMilli();
                statusRecencyIndex = (statusRecencyIndex + 1) % statusRecency;
                updateRecentThroughput(operationCountsAtDurations, settableRecentThroughputAndDuration);

                loggingService.status(
                    status,
                    settableRecentThroughputAndDuration,
                    completionTimeService.completionTimeAsMilli()
                );

                Spinner.powerNap(statusUpdateIntervalAsMilli);
            } catch (Throwable e) {
                errorReporter.reportError(
                    this,
                    format(
                        "Status reporting thread encountered unexpected error - exiting\n%s",
                        ConcurrentErrorReporter.stackTraceToString(e)
                    )
                );
                break;
            }
        }
    }

    public final synchronized void shutdown() {
        if (!continueRunning.get()) {
            return;
        }
        continueRunning.set(false);
    }

    private void updateRecentThroughput(final long[][] recentOperationCountsAtDurations,
                                        final SettableRecentThroughputAndDuration settableRecentThroughputAndDuration) {
        long minOperationCount = Long.MAX_VALUE;
        long maxOperationCount = Long.MIN_VALUE;
        long minDurationAsMilli = Long.MAX_VALUE;
        long maxDurationAsMilli = Long.MIN_VALUE;
        for (int i = 0; i < recentOperationCountsAtDurations.length; i++) {
            long operationCount = recentOperationCountsAtDurations[i][0];
            long durationAsMilli = recentOperationCountsAtDurations[i][1];
            if (-1 == operationCount) {
                continue;
            }
            minOperationCount = Math.min(minOperationCount, operationCount);
            maxOperationCount = Math.max(maxOperationCount, operationCount);
            minDurationAsMilli = Math.min(minDurationAsMilli, durationAsMilli);
            maxDurationAsMilli = Math.max(maxDurationAsMilli, durationAsMilli);
        }
        long recentRunDurationAsMilli = maxDurationAsMilli - minDurationAsMilli;
        long recentOperationCount = maxOperationCount - minOperationCount;
        double recentThroughput = (0 == recentRunDurationAsMilli)
            ? 0
            : (double) recentOperationCount / recentRunDurationAsMilli * 1000;
        settableRecentThroughputAndDuration.setThroughput(recentThroughput);
        settableRecentThroughputAndDuration.setDuration(recentRunDurationAsMilli);
    }

    private class SettableRecentThroughputAndDuration implements RecentThroughputAndDuration {
        private double throughput = 0.0;
        private long duration = 0;

        private void setThroughput(double throughput) {
            this.throughput = throughput;
        }

        private void setDuration(long duration) {
            this.duration = duration;
        }

        public double throughput() {
            return throughput;
        }

        public long duration() {
            return duration;
        }
    }
}
