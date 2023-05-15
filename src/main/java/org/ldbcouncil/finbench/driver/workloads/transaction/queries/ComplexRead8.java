package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload complex read query 8:
 * -- Transfer trace after loan applied --
 * Given a Loan and a specified time window between startTime and endTime, trace the fund transfer
or withdraw by at most 3 steps from the account the Loan deposits. Note that the transfer paths of
edge1, edge2, edge3 and edge4 are in a specific time range between startTime and endTime. Amount
of each transfers or withdrawals between the account and the upstream account should exceed a
specified threshold of the upstream transfer. Return all the accounts’ id in the downstream of loan
with the final ratio and distanceFromLoan.
Note: Upstream of an edge refers to the aggregated total amounts of all transfer-in edges of its
source Account.
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

public class ComplexRead8 extends LdbcOperation<List<ComplexRead8Result>> {
    public static final int TYPE = 8;
    public static final String ID = "id";
    public static final String THRESHOLD = "threshold";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String TRUNCATION_LIMIT = "truncationLimit";
    public static final String TRUNCATION_ORDER = "truncationOrder";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id;
    private final float threshold;
    private final Date startTime;
    private final Date endTime;
    private final int truncationLimit;
    private final TruncationOrder truncationOrder;

    public ComplexRead8(@JsonProperty(ID) long id,
                        @JsonProperty(THRESHOLD) float threshold,
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

    public ComplexRead8(ComplexRead8 operation) {
        this.id = operation.id;
        this.threshold = operation.threshold;
        this.startTime = operation.startTime;
        this.endTime = operation.endTime;
        this.truncationLimit = operation.truncationLimit;
        this.truncationOrder = operation.truncationOrder;
    }

    @Override
    public ComplexRead8 newInstance() {
        return new ComplexRead8(this);
    }

    public long getId() {
        return id;
    }

    public float getThreshold() {
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
    public List<ComplexRead8Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, ComplexRead8Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead8 that = (ComplexRead8) o;
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
        return "ComplexRead8{"
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

