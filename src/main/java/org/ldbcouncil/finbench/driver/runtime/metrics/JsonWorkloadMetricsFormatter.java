package org.ldbcouncil.finbench.driver.runtime.metrics;

import org.ldbcouncil.finbench.driver.formatter.WorkloadMetricsFormatter;

public class JsonWorkloadMetricsFormatter implements WorkloadMetricsFormatter {
    @Override
    public String format(WorkloadResultsSnapshot workloadResultsSnapshot) {
        return workloadResultsSnapshot.toJson();
    }
}
