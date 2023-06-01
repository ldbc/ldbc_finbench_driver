package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 10:
 * -- Add Guarantee Between Persons --
 * Add a *guarantee* edge from a *Person* node to another *Person* node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write10 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1010;
    public static final String PERSON_ID1 = "personId1";
    public static final String PERSON_ID2 = "personId2";
    public static final String TIME = "time";
    private final long personId1;
    private final long personId2;
    private final Date time;

    public Write10(@JsonProperty(PERSON_ID1) long personId1,
                   @JsonProperty(PERSON_ID2) long personId2,
                   @JsonProperty(TIME) Date time) {
        this.personId1 = personId1;
        this.personId2 = personId2;
        this.time = time;
    }

    public Write10(Write10 operation) {
        this.personId1 = operation.personId1;
        this.personId2 = operation.personId2;
        this.time = operation.time;
    }

    @Override
    public Write10 newInstance() {
        return new Write10(this);
    }

    public long getPersonId1() {
        return personId1;
    }

    public long getPersonId2() {
        return personId2;
    }

    public Date getTime() {
        return time;
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
            .put(TIME, time)
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
        Write10 that = (Write10) o;
        return personId1 == that.personId1
            && personId2 == that.personId2
            && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId1, personId2, time);
    }

    @Override
    public String toString() {
        return "Write10{"
            + "personId1="
            + personId1
            + ", personId2="
            + personId2
            + ", time="
            + time
            + '}';
    }
}

