package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead4Result {
    public static final String NUM_EDGE = "numEdge";
    private final int numEdge;

    public SimpleRead4Result(@JsonProperty(NUM_EDGE) int numEdge) {
        this.numEdge = numEdge;
    }

    public int getNumEdge() {
        return numEdge;
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
        return numEdge == that.numEdge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numEdge);
    }

    @Override
    public String toString() {
        return "SimpleRead4Result{"
            + "numEdge="
            + numEdge
            + '}';
    }
}

