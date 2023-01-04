package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 13:
 * -- Transfer to company amount statistics --
 *
 * Given a Person and a specified time window between start_time and end_time, find all the company
 * accounts that s/he has transferred to. Return companies and the sum of their transfer amount in
 * descending order.
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
import org.ldbcouncil.finbench.driver.truncation.TruncationOrder;

public class ComplexRead13 extends Operation<List<ComplexRead13Result>> {
    public static final int TYPE = 13;
    public static final String ID = "id";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String TRUNCATION_LIMIT = "truncation_limit";
    public static final String TRUNCATION_ORDER = "truncation_order";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id;
    private final Date startTime;
    private final Date endTime;
    private final int truncationLimit;
    private final TruncationOrder truncationOrder;

    public ComplexRead13(@JsonProperty("id") long id,
                         @JsonProperty("startTime") Date startTime,
                         @JsonProperty("endTime") Date endTime,
                         @JsonProperty("truncationLimit") int truncationLimit,
                         @JsonProperty("truncationOrder") TruncationOrder truncationOrder) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.truncationLimit = truncationLimit;
        this.truncationOrder = truncationOrder;
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

    public int getTruncationLimit() {
        return truncationLimit;
    }

    public TruncationOrder getTruncationOrder() {
        return truncationOrder;
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
            .put(TRUNCATION_LIMIT, truncationLimit)
            .put(TRUNCATION_ORDER, truncationOrder)
            .build();
    }

    @Override
    public List<ComplexRead13Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, ComplexRead13Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead13 that = (ComplexRead13) o;
        return id == that.id
            && truncationLimit == that.truncationLimit
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && truncationOrder == that.truncationOrder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, truncationLimit, truncationOrder);
    }

    @Override
    public String toString() {
        return "ComplexRead13{"
            + "id="
            + id
            + ", startTime="
            + startTime
            + ", endTime="
            + endTime
            + ", truncationLimit="
            + truncationLimit
            + ", truncationOrder="
            + truncationOrder
            + '}';
    }
}
