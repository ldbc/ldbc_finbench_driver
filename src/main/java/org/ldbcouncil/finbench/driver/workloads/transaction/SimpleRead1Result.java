package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead1Result {
    public static final String PROPERTIES = "properties";
    private final String properties;

    public SimpleRead1Result(@JsonProperty(PROPERTIES) String properties) {
        this.properties = properties;
    }

    public String getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead1Result that = (SimpleRead1Result) o;
        return Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }

    @Override
    public String toString() {
        return "SimpleRead1Result{"
            + "properties="
            + properties
            + '}';
    }
}

