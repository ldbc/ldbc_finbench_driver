package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload complex read query 4:
 * -- Three accounts in a transfer cycle --
 * Given two accounts src and dst, and a specified time window between startTime and endTime,
(1) check whether src transferred money to dst in the given time window (edge1)
(2) find all other accounts (other1, . . . , otherN) which received money from dst (edge2) and
transferred money to src (edge3) in a specific time.
For each of these other accounts, return the id of the account, the sum and max of the transfer
amount (edge2 and edge3).
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

public class ComplexRead4 extends LdbcOperation<List<ComplexRead4Result>> {
    public static final int TYPE = 4;
    public static final String ID1 = "id1";
    public static final String ID2 = "id2";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id1;
    private final long id2;
    private final Date startTime;
    private final Date endTime;

    public ComplexRead4(@JsonProperty(ID1) long id1,
                        @JsonProperty(ID2) long id2,
                        @JsonProperty(START_TIME) Date startTime,
                        @JsonProperty(END_TIME) Date endTime) {
        this.id1 = id1;
        this.id2 = id2;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ComplexRead4(ComplexRead4 operation) {
        this.id1 = operation.id1;
        this.id2 = operation.id2;
        this.startTime = operation.startTime;
        this.endTime = operation.endTime;
    }

    @Override
    public ComplexRead4 newInstance() {
        return new ComplexRead4(this);
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
    public List<ComplexRead4Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, ComplexRead4Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead4 that = (ComplexRead4) o;
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
        return "ComplexRead4{"
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

