package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload write query 14:
 * -- Remove a loan --
 * Given an id, remove a loan, and remove the repay, deposit edges.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.Operation;

public class Write14 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1014;
    public static final String ID = "id";
    private final long id;

    public Write14(@JsonProperty(ID) long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(ID, id)
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
        Write14 that = (Write14) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Write14{"
            + "id="
            + id
            + '}';
    }
}

