package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead3Result {
    public static final String SHORTEST_PATH_LENGTH = "shortestPathLength";
    private final long shortestPathLength;

    public ComplexRead3Result(@JsonProperty(SHORTEST_PATH_LENGTH) long shortestPathLength) {
        this.shortestPathLength = shortestPathLength;
    }

    public long getShortestPathLength() {
        return shortestPathLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead3Result that = (ComplexRead3Result) o;
        return shortestPathLength == that.shortestPathLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortestPathLength);
    }

    @Override
    public String toString() {
        return "ComplexRead3Result{"
            + "shortestPathLength="
            + shortestPathLength
            + '}';
    }
}

