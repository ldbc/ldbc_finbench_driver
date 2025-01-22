/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
