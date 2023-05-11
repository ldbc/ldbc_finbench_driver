package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload simple read query 2:
 * -- Transfer-ins and transfer-outs --
 * Given an account, find the sum and max of fund amount in transfer-ins and transfer-outs between
them in a specific time range between startTime and endTime. Return the sum and max of amount.
For edge1 and edge2, return -1 for the max (maxEdge1Amount and maxEdge2Amount) if there is no
transfer.
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
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class SimpleRead2 extends LdbcOperation<List<SimpleRead2Result>> {
    public static final int TYPE = 102;
    public static final String ID = "id";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id;
    private final Date startTime;
    private final Date endTime;

    public SimpleRead2(@JsonProperty(ID) long id,
                       @JsonProperty(START_TIME) Date startTime,
                       @JsonProperty(END_TIME) Date endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public SimpleRead2(SimpleRead2 operation) {
        this.id = operation.id;
        this.startTime = operation.startTime;
        this.endTime = operation.endTime;
    }

    @Override
    public SimpleRead2 newInstance() {
        return new SimpleRead2(this);
    }

    public long getId() {
        return id;
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
            .put(ID, id)
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
        return id == that.id
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime);
    }

    @Override
    public String toString() {
        return "SimpleRead2{"
            + "id="
            + id
            + ", startTime="
            + startTime
            + ", endTime="
            + endTime
            + '}';
    }
}

