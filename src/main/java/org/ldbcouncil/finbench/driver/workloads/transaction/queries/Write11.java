package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 11:
 * -- Block a person of high risk --
 * Set a person’s isBlocked to True.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;

public class Write11 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1011;
    public static final String PERSON_ID = "personId";
    private final long personId;

    public Write11(@JsonProperty(PERSON_ID) long personId) {
        this.personId = personId;
    }

    public Write11(Write11 operation) {
        this.personId = operation.personId;
    }

    @Override
    public Write11 newInstance() {
        return new Write11(this);
    }

    public long getPersonId() {
        return personId;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(PERSON_ID, personId)
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
        return personId == that.personId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId);
    }

    @Override
    public String toString() {
        return "Write11{"
            + "personId="
            + personId
            + '}';
    }
}

