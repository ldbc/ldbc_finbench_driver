package ldbc.finbench.driver.formatter;

import ldbc.finbench.driver.runtime.metrics.WorkloadResultsSnapshot;

public interface WorkloadMetricsFormatter {
    String format(WorkloadResultsSnapshot workloadResultsSnapshot);
}
