package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload complex read query 7:
 * -- Fast-in and Fast-out --
 * Given an Account and a specified time window between startTime and endTime, find all the transfer
in (edge1) and transfer-out (edge2) whose amount exceeds threshold. Return the count of src and
dst accounts and the ratio of transfer-in amount over transfer-out amount. The fast-in and fash-out
means a tight window between startTime and endTime. Return the ratio as -1 if there is no edge2.
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
import org.ldbcouncil.finbench.driver.truncation.TruncationOrder;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class ComplexRead7 extends LdbcOperation<List<ComplexRead7Result>> {
    public static final int TYPE = 7;
    public static final String ID = "id";
    public static final String THRESHOLD = "threshold";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String TRUNCATION_LIMIT = "truncationLimit";
    public static final String TRUNCATION_ORDER = "truncationOrder";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id;
    private final double threshold;
    private final Date startTime;
    private final Date endTime;
    private final int truncationLimit;
    private final TruncationOrder truncationOrder;

    public ComplexRead7(@JsonProperty(ID) long id,
                        @JsonProperty(THRESHOLD) double threshold,
                        @JsonProperty(START_TIME) Date startTime,
                        @JsonProperty(END_TIME) Date endTime,
                        @JsonProperty(TRUNCATION_LIMIT) int truncationLimit,
                        @JsonProperty(TRUNCATION_ORDER) TruncationOrder truncationOrder) {
        this.id = id;
        this.threshold = threshold;
        this.startTime = startTime;
        this.endTime = endTime;
        this.truncationLimit = truncationLimit;
        this.truncationOrder = truncationOrder;
    }

    public ComplexRead7(ComplexRead7 operation) {
        this.id = operation.id;
        this.threshold = operation.threshold;
        this.startTime = operation.startTime;
        this.endTime = operation.endTime;
        this.truncationLimit = operation.truncationLimit;
        this.truncationOrder = operation.truncationOrder;
    }

    @Override
    public ComplexRead7 newInstance() {
        return new ComplexRead7(this);
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
            .put(THRESHOLD, threshold)
            .put(START_TIME, startTime)
            .put(END_TIME, endTime)
            .put(TRUNCATION_LIMIT, truncationLimit)
            .put(TRUNCATION_ORDER, truncationOrder)
            .build();
    }

    @Override
    public List<ComplexRead7Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, ComplexRead7Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead7 that = (ComplexRead7) o;
        return id == that.id
            && threshold == that.threshold
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && truncationLimit == that.truncationLimit
            && truncationOrder == that.truncationOrder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, threshold, startTime, endTime, truncationLimit, truncationOrder);
    }

    @Override
    public String toString() {
        return "ComplexRead7{"
            + "id="
            + id
            + ", threshold="
            + threshold
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

