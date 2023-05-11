package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload complex read query 10:
 * -- Similarity of investor relationship --
 * Given two Persons and a specified time window between startTime and endTime, find all the Com
panies the two Persons invest in. Return the Jaccard similarity between the two companies set.
Return 0 if there is no edges found connecting to any of these two persons.
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

public class ComplexRead10 extends LdbcOperation<List<ComplexRead10Result>> {
    public static final int TYPE = 10;
    public static final String PID1 = "pid1";
    public static final String PID2 = "pid2";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long pid1;
    private final long pid2;
    private final Date startTime;
    private final Date endTime;

    public ComplexRead10(@JsonProperty(PID1) long pid1,
                         @JsonProperty(PID2) long pid2,
                         @JsonProperty(START_TIME) Date startTime,
                         @JsonProperty(END_TIME) Date endTime) {
        this.pid1 = pid1;
        this.pid2 = pid2;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ComplexRead10(ComplexRead10 operation) {
        this.pid1 = operation.pid1;
        this.pid2 = operation.pid2;
        this.startTime = operation.startTime;
        this.endTime = operation.endTime;
    }

    @Override
    public ComplexRead10 newInstance() {
        return new ComplexRead10(this);
    }

    public long getPid1() {
        return pid1;
    }

    public long getPid2() {
        return pid2;
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
            .put(PID1, pid1)
            .put(PID2, pid2)
            .put(START_TIME, startTime)
            .put(END_TIME, endTime)
            .build();
    }

    @Override
    public List<ComplexRead10Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, ComplexRead10Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead10 that = (ComplexRead10) o;
        return pid1 == that.pid1
            && pid2 == that.pid2
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid1, pid2, startTime, endTime);
    }

    @Override
    public String toString() {
        return "ComplexRead10{"
            + "pid1="
            + pid1
            + ", pid2="
            + pid2
            + ", startTime="
            + startTime
            + ", endTime="
            + endTime
            + '}';
    }
}

