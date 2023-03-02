package org.ldbcouncil.finbench.driver.runtime.metrics;

import static java.lang.String.format;

import com.lmax.disruptor.EventHandler;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;
import org.agrona.concurrent.UnsafeBuffer;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.log.LoggingServiceFactory;
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;
import org.ldbcouncil.finbench.driver.runtime.metrics.sbe.MetricsEvent;
import org.ldbcouncil.finbench.driver.temporal.TimeSource;

class DisruptorSbeMetricsEventHandler implements EventHandler<UnsafeBuffer> {
    private final AtomicStampedReference<WorkloadStatusSnapshot> statusSnapshotReference =
        new AtomicStampedReference<>(null, 0);
    private final AtomicStampedReference<WorkloadResultsSnapshot> resultsSnapshotReference =
        new AtomicStampedReference<>(null, 0);

    private final MetricsManager metricsManager;
    private final ConcurrentErrorReporter errorReporter;
    private final ResultsLogWriter resultsLogWriter;
    private long processedEventCount = 0L;
    private final String[] operationNames;
    private final MetricsEvent metricsEvent;

    DisruptorSbeMetricsEventHandler(
        ConcurrentErrorReporter errorReporter,
        ResultsLogWriter resultsLogWriter,
        TimeUnit unit,
        TimeSource timeSource,
        long maxRuntimeDurationAsNano,
        Map<Integer, Class<? extends Operation>> operationTypeToClassMapping,
        LoggingServiceFactory loggingServiceFactory) throws MetricsCollectionException {
        this.errorReporter = errorReporter;
        this.resultsLogWriter = resultsLogWriter;
        this.metricsManager = new MetricsManager(
            timeSource,
            unit,
            maxRuntimeDurationAsNano,
            operationTypeToClassMapping,
            loggingServiceFactory);
        operationNames = MetricsManager.toOperationNameArray(operationTypeToClassMapping);
        this.metricsEvent = new MetricsEvent();
    }

    AtomicStampedReference<WorkloadStatusSnapshot> statusSnapshot() {
        return statusSnapshotReference;
    }

    AtomicStampedReference<WorkloadResultsSnapshot> resultsSnapshot() {
        return resultsSnapshotReference;
    }

    long processedEventCount() {
        return processedEventCount;
    }

    @Override
    public void onEvent(UnsafeBuffer event, long l, boolean b) throws Exception {
        metricsEvent.wrapForDecode(
            event,
            DisruptorSbeMetricsEvent.MESSAGE_HEADER_SIZE,
            DisruptorSbeMetricsEvent.ACTING_BLOCK_LENGTH,
            DisruptorSbeMetricsEvent.ACTING_VERSION
        );

        switch (metricsEvent.eventType()) {
            case DisruptorSbeMetricsEvent.SUBMIT_OPERATION_RESULT: {
                int operationType = metricsEvent.operationType();
                long scheduledStartTimeAsMilli = metricsEvent.scheduledStartTimeAsMilli();
                long actualStartTimeAsMilli = metricsEvent.actualStartTimeAsMilli();
                long runDurationAsNano = metricsEvent.runDurationAsNano();
                int resultCode = metricsEvent.resultCode();
                long originalStartTime = metricsEvent.originalStartTime();

                resultsLogWriter.write(
                    operationNames[operationType],
                    scheduledStartTimeAsMilli,
                    actualStartTimeAsMilli,
                    runDurationAsNano,
                    resultCode,
                    originalStartTime);

                metricsManager.measure(actualStartTimeAsMilli, runDurationAsNano, operationType);
                processedEventCount++;
                break;
            }
            case DisruptorSbeMetricsEvent.GET_WORKLOAD_STATUS: {
                WorkloadStatusSnapshot newStatus = metricsManager.status();
                WorkloadStatusSnapshot oldStatus;
                int oldStamp;
                do {
                    oldStatus = statusSnapshotReference.getReference();
                    oldStamp = statusSnapshotReference.getStamp();
                } while (!statusSnapshotReference.compareAndSet(oldStatus, newStatus, oldStamp, oldStamp + 1));
                break;
            }
            case DisruptorSbeMetricsEvent.GET_WORKLOAD_RESULTS: {
                WorkloadResultsSnapshot newResults = metricsManager.snapshot();
                WorkloadResultsSnapshot oldResults;
                int oldStamp;
                do {
                    oldResults = resultsSnapshotReference.getReference();
                    oldStamp = resultsSnapshotReference.getStamp();
                } while (!resultsSnapshotReference.compareAndSet(oldResults,
                    newResults, oldStamp, oldStamp + 1));
                break;
            }
            default: {
                errorReporter.reportError(this, format("Encountered unexpected event: %s", event.toString()));
                break;
            }
        }
    }
}
