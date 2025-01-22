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
