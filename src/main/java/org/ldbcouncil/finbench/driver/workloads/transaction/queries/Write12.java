package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 12:
 * -- Block a medium of high risk --
 * Set an existed medium’s isBlocked to True.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;

public class Write12 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1012;
    public static final String MEDIUM_ID = "mediumId";
    private final long mediumId;

    public Write12(@JsonProperty(MEDIUM_ID) long mediumId) {
        this.mediumId = mediumId;
    }

    public long getMediumId() {
        return mediumId;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(MEDIUM_ID, mediumId)
            .build();
    }

    @Override
    public LdbcNoResult deserializeResult(String serializedResults) {
        return LdbcNoResult.INSTANCE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Write12 that = (Write12) o;
        return mediumId == that.mediumId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mediumId);
    }

    @Override
    public String toString() {
        return "Write12{"
            + "mediumId="
            + mediumId
            + '}';
    }
}

