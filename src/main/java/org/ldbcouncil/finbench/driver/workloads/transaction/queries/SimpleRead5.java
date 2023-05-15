package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload simple read query 5:
 * -- Account transfer-ins over threshold --
 * Given an account (dst), find all the transfer-ins (edge) from the src to a dst where the amount
exceeds threshold in a specific time range between startTime and endTime. Return the count of
transfer-ins and the amount sum.
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

public class SimpleRead5 extends LdbcOperation<List<SimpleRead5Result>> {
    public static final int TYPE = 105;
    public static final String ID = "id";
    public static final String THRESHOLD = "threshold";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id;
    private final double threshold;
    private final Date startTime;
    private final Date endTime;

    public SimpleRead5(@JsonProperty(ID) long id,
                       @JsonProperty(THRESHOLD) double threshold,
                       @JsonProperty(START_TIME) Date startTime,
                       @JsonProperty(END_TIME) Date endTime) {
        this.id = id;
        this.threshold = threshold;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public SimpleRead5(SimpleRead5 operation) {
        this.id = operation.id;
        this.threshold = operation.threshold;
        this.startTime = operation.startTime;
        this.endTime = operation.endTime;
    }

    @Override
    public SimpleRead5 newInstance() {
        return new SimpleRead5(this);
    }

    public long getId() {
        return id;
    }

    public double getThreshold() {
        return threshold;
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
            .put(THRESHOLD, threshold)
            .put(START_TIME, startTime)
            .put(END_TIME, endTime)
            .build();
    }

    @Override
    public List<SimpleRead5Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, SimpleRead5Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead5 that = (SimpleRead5) o;
        return id == that.id
            && threshold == that.threshold
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, threshold, startTime, endTime);
    }

    @Override
    public String toString() {
        return "SimpleRead5{"
            + "id="
            + id
            + ", threshold="
            + threshold
            + ", startTime="
            + startTime
            + ", endTime="
            + endTime
            + '}';
    }
}

