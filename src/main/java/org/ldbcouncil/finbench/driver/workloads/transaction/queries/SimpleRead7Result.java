package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead7Result {
    public static final String DST_ACCOUNT_ID = "dstAccountId";
    private final long dstAccountId;

    public SimpleRead7Result(@JsonProperty(DST_ACCOUNT_ID) long dstAccountId) {
        this.dstAccountId = dstAccountId;
    }

    public long getDstAccountId() {
        return dstAccountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead7Result that = (SimpleRead7Result) o;
        return dstAccountId == that.dstAccountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dstAccountId);
    }

    @Override
    public String toString() {
        return "SimpleRead7Result{"
            + "dstAccountId="
            + dstAccountId
            + '}';
    }
}

