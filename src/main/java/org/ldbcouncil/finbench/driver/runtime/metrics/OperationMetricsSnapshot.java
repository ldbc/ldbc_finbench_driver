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
