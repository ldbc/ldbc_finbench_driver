package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload complex read query 5:
 * -- Exact Account Transfer Trace --
 * Given a Person and a specified time window between startTime and endTime, find the transfer trace
from the account (src) owned by the Person to another account (dst) by at most 3 steps. Note that
the trace (edge2) must be ascending order of their timestamps. Return all the transfer traces.
Note: Multiple edges of from the same src to the same dst should be seen as identical path. And
the resulting paths shall not include recurring accounts (cycles in the trace are not allowed).
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.truncation.TruncationOrder;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class ComplexRead5 extends LdbcOperation<List<ComplexRead5Result>> {
    public static final int TYPE = 5;
    public static final String ID = "id";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String TRUNCATION_LIMIT = "truncationLimit";
    public static final String TRUNCATION_ORDER = "truncationOrder";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id;
    private final Date startTime;
    private final Date endTime;
    private final int truncationLimit;
    private final TruncationOrder truncationOrder;

    public ComplexRead5(@JsonProperty(ID) long id,
                        @JsonProperty(START_TIME) Date startTime,
                        @JsonProperty(END_TIME) Date endTime,
                        @JsonProperty(TRUNCATION_LIMIT) int truncationLimit,
                        @JsonProperty(TRUNCATION_ORDER) TruncationOrder truncationOrder) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.truncationLimit = truncationLimit;
        this.truncationOrder = truncationOrder;
    }

    public ComplexRead5(ComplexRead5 operation) {
        this.id = operation.id;
        this.startTime = operation.startTime;
        this.endTime = operation.endTime;
        this.truncationLimit = operation.truncationLimit;
        this.truncationOrder = operation.truncationOrder;
    }

    @Override
    public ComplexRead5 newInstance() {
        return new ComplexRead5(this);
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
    public List<ComplexRead5Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, ComplexRead5Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead5 that = (ComplexRead5) o;
        return id == that.id
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && truncationLimit == that.truncationLimit
            && truncationOrder == that.truncationOrder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, truncationLimit, truncationOrder);
    }

    @Override
    public String toString() {
        return "ComplexRead5{"
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

    public void resultSort(List<ComplexRead5Result> result) {
        result.sort(Comparator.comparingLong(ComplexRead5Result::hashCode));
    }
}

