package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload complex read query 6:
 * -- Withdrawal after Many-to-One transfer --
 * Given an account of type card and a specified time window between startTime and endTime, find all
the connected accounts (mid) via withdrawal (edge2) satisfying, (1) More than 3 transfer-ins (edge1)
from other accounts (src) whose amount exceeds threshold1. (2) The amount of withdrawal (edge2)
from mid to dstCard whose exceeds threshold2. Return the sum of transfer amount from src to mid,
the amount from mid to dstCard grouped by mid.
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

public class ComplexRead6 extends LdbcOperation<List<ComplexRead6Result>> {
    public static final int TYPE = 6;
    public static final String ID = "id";
    public static final String THRESHOLD1 = "threshold1";
    public static final String THRESHOLD2 = "threshold2";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String TRUNCATION_LIMIT = "truncationLimit";
    public static final String TRUNCATION_ORDER = "truncationOrder";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id;
    private final double threshold1;
    private final double threshold2;
    private final Date startTime;
    private final Date endTime;
    private final int truncationLimit;
    private final TruncationOrder truncationOrder;

    public ComplexRead6(@JsonProperty(ID) long id,
                        @JsonProperty(THRESHOLD1) double threshold1,
                        @JsonProperty(THRESHOLD2) double threshold2,
                        @JsonProperty(START_TIME) Date startTime,
                        @JsonProperty(END_TIME) Date endTime,
                        @JsonProperty(TRUNCATION_LIMIT) int truncationLimit,
                        @JsonProperty(TRUNCATION_ORDER) TruncationOrder truncationOrder) {
        this.id = id;
        this.threshold1 = threshold1;
        this.threshold2 = threshold2;
        this.startTime = startTime;
        this.endTime = endTime;
        this.truncationLimit = truncationLimit;
        this.truncationOrder = truncationOrder;
    }

    public ComplexRead6(ComplexRead6 operation) {
        this.id = operation.id;
        this.threshold1 = operation.threshold1;
        this.threshold2 = operation.threshold2;
        this.startTime = operation.startTime;
        this.endTime = operation.endTime;
        this.truncationLimit = operation.truncationLimit;
        this.truncationOrder = operation.truncationOrder;
    }

    @Override
    public ComplexRead6 newInstance() {
        return new ComplexRead6(this);
    }

    public long getId() {
        return id;
    }

    public double getThreshold1() {
        return threshold1;
    }

    public double getThreshold2() {
        return threshold2;
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
            .put(THRESHOLD1, threshold1)
            .put(THRESHOLD2, threshold2)
            .put(START_TIME, startTime)
            .put(END_TIME, endTime)
            .put(TRUNCATION_LIMIT, truncationLimit)
            .put(TRUNCATION_ORDER, truncationOrder)
            .build();
    }

    @Override
    public List<ComplexRead6Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, ComplexRead6Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead6 that = (ComplexRead6) o;
        return id == that.id
            && threshold1 == that.threshold1
            && threshold2 == that.threshold2
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && truncationLimit == that.truncationLimit
            && truncationOrder == that.truncationOrder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, threshold1, threshold2, startTime, endTime, truncationLimit, truncationOrder);
    }

    @Override
    public String toString() {
        return "ComplexRead6{"
            + "id="
            + id
            + ", threshold1="
            + threshold1
            + ", threshold2="
            + threshold2
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

