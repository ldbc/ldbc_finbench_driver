package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 18:
 * -- Block a Person of high risk --
 *  Set a *Person*'s isBlocked to True.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write18 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1019;
    public static final String PERSON_ID = "personId";
    private final long personId;

    public Write18(@JsonProperty(PERSON_ID) long personId) {
        this.personId = personId;
    }

    public Write18(Write18 operation) {
        this.personId = operation.personId;
    }

    @Override
    public Write18 newInstance() {
        return new Write18(this);
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
        Write18 that = (Write18) o;
        return personId == that.personId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId);
    }

    @Override
    public String toString() {
        return "Write18{"
            + "personId="
            + personId
            + '}';
    }
}

