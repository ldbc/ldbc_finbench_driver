package org.ldbcouncil.finbench.driver.runtime.metrics;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class DiscreteMetricSnapshot {
    @JsonProperty("name")
    private String name;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("count")
    private long count;
    @JsonProperty("all_values")
    private Map<Long, Long> allValues;

    private DiscreteMetricSnapshot() {
    }

    DiscreteMetricSnapshot(String name, String unit, long count, Map<Long, Long> allValues) {
        this.name = name;
        this.unit = unit;
        this.count = count;
        this.allValues = allValues;
    }

    public String name() {
        return name;
    }

    public String unit() {
        return unit;
    }

    public long count() {
        return count;
    }

    public Map<Long, Long> allValues() {
        return allValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DiscreteMetricSnapshot that = (DiscreteMetricSnapshot) o;

        if (count != that.count) {
            return false;
        }
        if (allValues != null ? !allValues.equals(that.allValues) : that.allValues != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        return unit != null ? unit.equals(that.unit) : that.unit == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (int) (count ^ (count >>> 32));
        result = 31 * result + (allValues != null ? allValues.hashCode() : 0);
        return result;
    }
}
