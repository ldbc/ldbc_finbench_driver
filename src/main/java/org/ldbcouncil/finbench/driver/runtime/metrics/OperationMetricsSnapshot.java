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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OperationMetricsSnapshot {
    @JsonProperty("name")
    private String name;
    @JsonProperty("unit")
    private TimeUnit durationUnit;
    @JsonProperty("count")
    private long count;
    @JsonProperty("run_time")
    private ContinuousMetricSnapshot runTimeMetric;

    private OperationMetricsSnapshot() {
    }

    public OperationMetricsSnapshot(String name,
                                    TimeUnit durationUnit,
                                    long count,
                                    ContinuousMetricSnapshot runTimeMetric) {
        this.name = name;
        this.durationUnit = durationUnit;
        this.count = count;
        this.runTimeMetric = runTimeMetric;
    }

    public String name() {
        return name;
    }

    public TimeUnit durationUnit() {
        return durationUnit;
    }

    public long count() {
        return count;
    }

    public ContinuousMetricSnapshot runTimeMetric() {
        return runTimeMetric;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OperationMetricsSnapshot that = (OperationMetricsSnapshot) o;

        if (count != that.count) {
            return false;
        }
        if (durationUnit != that.durationUnit) {
            return false;
        }
        if (!Objects.equals(name, that.name)) {
            return false;
        }
        return Objects.equals(runTimeMetric, that.runTimeMetric);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (durationUnit != null ? durationUnit.hashCode() : 0);
        result = 31 * result + (int) (count ^ (count >>> 32));
        result = 31 * result + (runTimeMetric != null ? runTimeMetric.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OperationMetricsSnapshot{" + "name='" + name + '\'' + ", durationUnit=" + durationUnit + ", count="
            + count + ", runTimeMetric=" + runTimeMetric + '}';
    }
}
