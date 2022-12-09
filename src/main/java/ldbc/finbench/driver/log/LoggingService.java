package ldbc.finbench.driver.log;

import ldbc.finbench.driver.runtime.metrics.WorkloadResultsSnapshot;
import ldbc.finbench.driver.runtime.metrics.WorkloadStatusSnapshot;

public interface LoggingService {
    void info(String message);

    void status(WorkloadStatusSnapshot workloadStatusSnapshot,
                RecentThroughputAndDuration recentThroughputAndDuration,
                long completionTimeAsMilli);

    void summaryResult(WorkloadResultsSnapshot workloadResultsSnapshot);

    void detailedResult(WorkloadResultsSnapshot workloadResultsSnapshot);
}
