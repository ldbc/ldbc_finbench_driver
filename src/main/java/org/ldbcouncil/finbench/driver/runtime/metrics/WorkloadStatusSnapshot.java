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

public class WorkloadStatusSnapshot {
    private final long runDurationAsMilli;
    private final long operationCount;
    private final long durationSinceLastMeasurementAsMilli;
    private final double throughput;

    public WorkloadStatusSnapshot(long runDurationAsMilli,
                                  long operationCount,
                                  long durationSinceLastMeasurementAsMilli,
                                  double throughput) {
        this.runDurationAsMilli = runDurationAsMilli;
        this.operationCount = operationCount;
        this.durationSinceLastMeasurementAsMilli = durationSinceLastMeasurementAsMilli;
        this.throughput = throughput;
    }

    public long runDurationAsMilli() {
        return runDurationAsMilli;
    }

    public long operationCount() {
        return operationCount;
    }

    public long durationSinceLastMeasurementAsMilli() {
        return durationSinceLastMeasurementAsMilli;
    }

    public double throughput() {
        return throughput;
    }
}
