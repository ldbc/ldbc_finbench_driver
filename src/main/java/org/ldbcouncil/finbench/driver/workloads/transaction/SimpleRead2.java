package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload simple read query 2:
 * -- Transfers between two accounts --
 * Given two Accounts, find the sum and max of fund amount in transfer between them in a specific
time range between start_time and end_time. Return the sum and max of amount of e1 and e2 in
seperate.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.Operation;

public class SimpleRead2 extends Operation<List<SimpleRead2Result>> {
    public static final int TYPE = 102;
    public static final String ID1 = "id1";
    public static final String ID2 = "id2";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id1;
    private final long id2;
    private final Date startTime;
    private final Date endTime;

    public SimpleRead2(@JsonProperty(ID1) long id1,
                       @JsonProperty(ID2) long id2,
                       @JsonProperty(START_TIME) Date startTime,
                       @JsonProperty(END_TIME) Date endTime) {
        this.id1 = id1;
        this.id2 = id2;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getId1() {
        return id1;
    }

    public long getId2() {
        return id2;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(ID1, id1)
            .put(ID2, id2)
            .put(START_TIME, startTime)
            .put(END_TIME, endTime)
            .build();
    }

    @Override
    public List<SimpleRead2Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, SimpleRead2Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead2 that = (SimpleRead2) o;
        return id1 == that.id1
            && id2 == that.id2
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2, startTime, endTime);
    }

    @Override
    public String toString() {
        return "SimpleRead2{"
            + "id1="
            + id1
            + ", id2="
            + id2
            + ", startTime="
            + startTime
            + ", endTime="
            + endTime
            + '}';
    }
}

