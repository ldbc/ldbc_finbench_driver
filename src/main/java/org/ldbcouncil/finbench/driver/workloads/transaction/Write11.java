package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload write query 11:
 * -- Block a medium of high risk --
 * Set an existed medium’s isBlocked to True.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.Operation;

public class Write11 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1011;
    public static final String MEDIUM_ID = "mediumId";
    private final long mediumId;

    public Write11(@JsonProperty(MEDIUM_ID) long mediumId) {
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
        Write11 that = (Write11) o;
        return mediumId == that.mediumId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mediumId);
    }

    @Override
    public String toString() {
        return "Write11{"
            + "mediumId="
            + mediumId
            + '}';
    }
}

