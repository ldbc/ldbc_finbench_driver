package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload write query 12:
 * -- Add guarantee between persons --
 * Add a guarantee edge from an existed person node to another existed person node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.Operation;

public class Write12 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1012;
    public static final String PERSON_ID1 = "personId1";
    public static final String PERSON_ID2 = "personId2";
    public static final String CURRENT_TIME = "currentTime";
    private final long personId1;
    private final long personId2;
    private final Date currentTime;

    public Write12(@JsonProperty(PERSON_ID1) long personId1,
                   @JsonProperty(PERSON_ID2) long personId2,
                   @JsonProperty(CURRENT_TIME) Date currentTime) {
        this.personId1 = personId1;
        this.personId2 = personId2;
        this.currentTime = currentTime;
    }

    public long getPersonId1() {
        return personId1;
    }

    public long getPersonId2() {
        return personId2;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(PERSON_ID1, personId1)
            .put(PERSON_ID2, personId2)
            .put(CURRENT_TIME, currentTime)
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
        return personId1 == that.personId1
            && personId2 == that.personId2
            && Objects.equals(currentTime, that.currentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId1, personId2, currentTime);
    }

    @Override
    public String toString() {
        return "Write12{"
            + "personId1="
            + personId1
            + ", personId2="
            + personId2
            + ", currentTime="
            + currentTime
            + '}';
    }
}

