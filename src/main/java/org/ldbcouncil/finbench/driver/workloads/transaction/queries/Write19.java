package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 19:
 * -- Block a Person of high risk --
 *  Set a *Person*'s isBlocked to True.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write19 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1019;
    public static final String PERSON_ID = "personId";
    private final long personId;

    public Write19(@JsonProperty(PERSON_ID) long personId) {
        this.personId = personId;
    }

    public Write19(Write19 operation) {
        this.personId = operation.personId;
    }

    @Override
    public Write19 newInstance() {
        return new Write19(this);
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
        Write19 that = (Write19) o;
        return personId == that.personId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId);
    }

    @Override
    public String toString() {
        return "Write19{"
            + "personId="
            + personId
            + '}';
    }
}

