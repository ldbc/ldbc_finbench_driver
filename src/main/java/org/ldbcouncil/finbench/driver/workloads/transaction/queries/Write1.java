package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 1:
 * -- Add a Person Node --
 * Add a Person node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write1 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1001;
    public static final String PERSON_ID = "personId";
    public static final String PERSON_NAME = "personName";
    public static final String IS_BLOCKED = "isBlocked";
    private final long personId;
    private final String personName;
    private final boolean isBlocked;

    public Write1(@JsonProperty(PERSON_ID) long personId,
                  @JsonProperty(PERSON_NAME) String personName,
                  @JsonProperty(IS_BLOCKED) boolean isBlocked) {
        this.personId = personId;
        this.personName = personName;
        this.isBlocked = isBlocked;
    }

    public Write1(Write1 operation) {
        this.personId = operation.personId;
        this.personName = operation.personName;
        this.isBlocked = operation.isBlocked;
    }

    @Override
    public Write1 newInstance() {
        return new Write1(this);
    }

    public long getPersonId() {
        return personId;
    }

    public String getPersonName() {
        return personName;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(PERSON_ID, personId)
            .put(PERSON_NAME, personName)
            .put(IS_BLOCKED, isBlocked)
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
        Write1 that = (Write1) o;
        return personId == that.personId
            && Objects.equals(personName, that.personName)
            && isBlocked == that.isBlocked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, personName, isBlocked);
    }

    @Override
    public String toString() {
        return "Write1{"
            + "personId="
            + personId
            + ", personName="
            + personName
            + ", isBlocked="
            + isBlocked
            + '}';
    }
}

