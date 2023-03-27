package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead6Result {
    public static final String DST_ID = "dstId";
    private final long dstId;

    public SimpleRead6Result(@JsonProperty(DST_ID) long dstId) {
        this.dstId = dstId;
    }

    public long getDstId() {
        return dstId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead6Result that = (SimpleRead6Result) o;
        return dstId == that.dstId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dstId);
    }

    @Override
    public String toString() {
        return "SimpleRead6Result{"
            + "dstId="
            + dstId
            + '}';
    }
}

