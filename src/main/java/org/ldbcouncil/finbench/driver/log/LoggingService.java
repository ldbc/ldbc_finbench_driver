package org.ldbcouncil.finbench.driver.log;

import org.ldbcouncil.finbench.driver.runtime.metrics.WorkloadResultsSnapshot;
import org.ldbcouncil.finbench.driver.runtime.metrics.WorkloadStatusSnapshot;

public interface LoggingService {
    void info(String message);

    void status(WorkloadStatusSnapshot workloadStatusSnapshot,
                RecentThroughputAndDuration recentThroughputAndDuration,
                long completionTimeAsMilli);

    void summaryResult(WorkloadResultsSnapshot workloadResultsSnapshot);

    void detailedResult(WorkloadResultsSnapshot workloadResultsSnapshot);
}
