package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload complex read query 3:
 * -- Shortest transfer path --
 * Given two accounts and a specified time window between startTime and endTime, find the length
of shortest path between these two accounts by the transfer relationships. Note that all the edges
in the path should be in the time window and of type transfer. Return 1 if src and dst are directly
connected. Return -1 if there is no path found.
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

public class ComplexRead3 extends LdbcOperation<List<ComplexRead3Result>> {
    public static final int TYPE = 3;
    public static final String ID1 = "id1";
    public static final String ID2 = "id2";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id1;
    private final long id2;
    private final Date startTime;
    private final Date endTime;

    public ComplexRead3(@JsonProperty(ID1) long id1,
                        @JsonProperty(ID2) long id2,
                        @JsonProperty(START_TIME) Date startTime,
                        @JsonProperty(END_TIME) Date endTime) {
        this.id1 = id1;
        this.id2 = id2;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ComplexRead3(ComplexRead3 operation) {
        this.id1 = operation.id1;
        this.id2 = operation.id2;
        this.startTime = operation.startTime;
        this.endTime = operation.endTime;
    }

    @Override
    public ComplexRead3 newInstance() {
        return new ComplexRead3(this);
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
    public List<ComplexRead3Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, ComplexRead3Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead3 that = (ComplexRead3) o;
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
        return "ComplexRead3{"
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

