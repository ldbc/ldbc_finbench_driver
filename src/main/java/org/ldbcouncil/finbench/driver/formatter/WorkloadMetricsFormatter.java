package org.ldbcouncil.finbench.driver.formatter;

import org.ldbcouncil.finbench.driver.runtime.metrics.WorkloadResultsSnapshot;

public interface WorkloadMetricsFormatter {
    String format(WorkloadResultsSnapshot workloadResultsSnapshot);
}
