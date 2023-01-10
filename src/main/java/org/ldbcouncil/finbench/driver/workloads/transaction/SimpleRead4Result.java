package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead4Result {
    public static final String COUNT_EDGE = "countEdge";
    private final int countEdge;

    public SimpleRead4Result(@JsonProperty(COUNT_EDGE) int countEdge) {
        this.countEdge = countEdge;
    }

    public int getCountEdge() {
        return countEdge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead4Result that = (SimpleRead4Result) o;
        return countEdge == that.countEdge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(countEdge);
    }

    @Override
    public String toString() {
        return "SimpleRead4Result{"
            + "countEdge="
            + countEdge
            + '}';
    }
}

