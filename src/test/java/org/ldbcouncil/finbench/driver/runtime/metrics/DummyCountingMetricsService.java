package org.ldbcouncil.finbench.driver.runtime.metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DummyCountingMetricsService implements MetricsService, MetricsService.MetricsServiceWriter {
    private final Map<String, OperationMetricsSnapshot> metrics;
    private long count = 0;

    public DummyCountingMetricsService() {
        metrics = new HashMap<>();
        metrics.put("default", new OperationMetricsSnapshot(null, null, 0, null));
    }

    @Override
    public void submitOperationResult(int operationType,
                                      long scheduledStartTimeAsMilli,
                                      long actualStartTimeAsMilli,
                                      long runDurationAsNano,
                                      int resultCode,
                                      long originalStartTime) throws MetricsCollectionException {
        count++;
    }

    public long count() {
        return count;
    }

    @Override
    public WorkloadStatusSnapshot status() throws MetricsCollectionException {
        return new WorkloadStatusSnapshot(-1, count, -1, 0);
    }

    @Override
    public WorkloadResultsSnapshot results() throws MetricsCollectionException {
        return new WorkloadResultsSnapshot(metrics.values(), 0, 0, count, TimeUnit.MILLISECONDS);
    }

    @Override
    public void shutdown() throws MetricsCollectionException {

    }

    @Override
    public MetricsServiceWriter getWriter() {
        return this;
    }
}
