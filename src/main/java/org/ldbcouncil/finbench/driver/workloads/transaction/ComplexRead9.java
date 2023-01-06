package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 9:
 * -- Money laundering with loan involved --
 * Given an account and a specified time window between start_time and end_time, find the despoit
and repay edge between the account and a loan, the transfers-in and transfers-out. Then calculate the
amount ratio of deposit over repay, the amount ratio of transfers-in over transfers-out, the amount
ratio of deposit over transfers-out. Return the ratios.
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

public class ComplexRead9 extends Operation<List<ComplexRead9Result>> {
    public static final int TYPE = 9;
    public static final String THRESHOLD = "threshold";
    public static final String LOWER_BOUND = "lowerBound";
    public static final String UPPER_BOUND = "upperBound";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String TRUNCATION_LIMIT = "truncationLimit";
    public static final String TRUNCATION_ORDER = "truncationOrder";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long threshold;
    private final float lowerBound;
    private final float upperBound;
    private final Date startTime;
    private final Date endTime;
    private final int truncationLimit;
    private final TruncationOrder truncationOrder;

    public ComplexRead9(@JsonProperty(THRESHOLD) long threshold,
                        @JsonProperty(LOWER_BOUND) float lowerBound,
                        @JsonProperty(UPPER_BOUND) float upperBound,
                        @JsonProperty(START_TIME) Date startTime,
                        @JsonProperty(END_TIME) Date endTime,
                        @JsonProperty(TRUNCATION_LIMIT) int truncationLimit,
                        @JsonProperty(TRUNCATION_ORDER) TruncationOrder truncationOrder) {
        this.threshold = threshold;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.startTime = startTime;
        this.endTime = endTime;
        this.truncationLimit = truncationLimit;
        this.truncationOrder = truncationOrder;
    }

    public long getThreshold() {
        return threshold;
    }

    public float getLowerBound() {
        return lowerBound;
    }

    public float getUpperBound() {
        return upperBound;
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
            .put(THRESHOLD, threshold)
            .put(LOWER_BOUND, lowerBound)
            .put(UPPER_BOUND, upperBound)
            .put(START_TIME, startTime)
            .put(END_TIME, endTime)
            .put(TRUNCATION_LIMIT, truncationLimit)
            .put(TRUNCATION_ORDER, truncationOrder)
            .build();
    }

    @Override
    public List<ComplexRead9Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, ComplexRead9Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead9 that = (ComplexRead9) o;
        return threshold == that.threshold
            && lowerBound == that.lowerBound
            && upperBound == that.upperBound
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && truncationLimit == that.truncationLimit
            && truncationOrder == that.truncationOrder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(threshold, lowerBound, upperBound, startTime, endTime, truncationLimit, truncationOrder);
    }

    @Override
    public String toString() {
        return "ComplexRead9{"
            + "threshold="
            + threshold
            + ", lowerBound="
            + lowerBound
            + ", upperBound="
            + upperBound
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

