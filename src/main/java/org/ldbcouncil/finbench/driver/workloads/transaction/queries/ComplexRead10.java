package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload complex read query 10:
 * -- Similarity of investor relationship --
 * Given two Persons and a specified time window between start_time and end_time, find all the
Companies the two Persons invest. Return the jaccard similarity between the two companies set.
Note that there is no truncation in this case.
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

public class ComplexRead10 extends Operation<List<ComplexRead10Result>> {
    public static final int TYPE = 10;
    public static final String ID1 = "id1";
    public static final String ID2 = "id2";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id1;
    private final long id2;
    private final Date startTime;
    private final Date endTime;

    public ComplexRead10(@JsonProperty(ID1) long id1,
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
        return "ComplexRead10{"
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

