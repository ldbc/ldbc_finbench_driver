package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload read write query 2:
 * -- Transfer under fast-in and fast-out strategy --
 * The workflow of this read write query contains at least one transaction. It works as:
• In the very beginning, read the blocked status of related accounts. The transaction aborts
if one of them is blocked. Move to the next step if none is blocked.
• Add a transfer edge inside the transaction.
• Detect if a fast-in and fast-out pattern formed, both for the src and dst account. Transaction
aborts if formed, and then mark the related accounts as blocked in another transaction.
Otherwise the transaction commits.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class ReadWrite2 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 10002;
    public static final String SRC_ID = "srcId";
    public static final String DST_ID = "dstId";
    public static final String TIME = "time";
    public static final String AMOUNT = "amount";
    public static final String AMOUNT_THRESHOLD = "amountThreshold";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String RATIO_THRESHOLD = "ratioThreshold";
    private final long srcId;
    private final long dstId;
    private final Date time;
    private final long amount;
    private final long amountThreshold;
    private final Date startTime;
    private final Date endTime;
    private final float ratioThreshold;

    public ReadWrite2(@JsonProperty(SRC_ID) long srcId,
                      @JsonProperty(DST_ID) long dstId,
                      @JsonProperty(TIME) Date time,
                      @JsonProperty(AMOUNT) long amount,
                      @JsonProperty(AMOUNT_THRESHOLD) long amountThreshold,
                      @JsonProperty(START_TIME) Date startTime,
                      @JsonProperty(END_TIME) Date endTime,
                      @JsonProperty(RATIO_THRESHOLD) float ratioThreshold) {
        this.srcId = srcId;
        this.dstId = dstId;
        this.time = time;
        this.amount = amount;
        this.amountThreshold = amountThreshold;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ratioThreshold = ratioThreshold;
    }

    public ReadWrite2(ReadWrite2 operation) {
        this.srcId = operation.srcId;
        this.dstId = operation.dstId;
        this.time = operation.time;
        this.amount = operation.amount;
        this.amountThreshold = operation.amountThreshold;
        this.startTime = operation.startTime;
        this.endTime = operation.endTime;
        this.ratioThreshold = operation.ratioThreshold;
    }

    @Override
    public ReadWrite2 newInstance() {
        return new ReadWrite2(this);
    }

    public long getSrcId() {
        return srcId;
    }

    public long getDstId() {
        return dstId;
    }

    public Date getTime() {
        return time;
    }

    public long getAmount() {
        return amount;
    }

    public long getAmountThreshold() {
        return amountThreshold;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public float getRatioThreshold() {
        return ratioThreshold;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(SRC_ID, srcId)
            .put(DST_ID, dstId)
            .put(TIME, time)
            .put(AMOUNT, amount)
            .put(AMOUNT_THRESHOLD, amountThreshold)
            .put(START_TIME, startTime)
            .put(END_TIME, endTime)
            .put(RATIO_THRESHOLD, ratioThreshold)
            .build();
    }

    @Override
    public LdbcNoResult deserializeResult(String serializedResults) {
        return LdbcNoResult.INSTANCE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReadWrite2 that = (ReadWrite2) o;
        return srcId == that.srcId
            && dstId == that.dstId
            && Objects.equals(time, that.time)
            && amount == that.amount
            && amountThreshold == that.amountThreshold
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && ratioThreshold == that.ratioThreshold;
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcId, dstId, time, amount, amountThreshold, startTime, endTime, ratioThreshold);
    }

    @Override
    public String toString() {
        return "ReadWrite2{"
            + "srcId="
            + srcId
            + ", dstId="
            + dstId
            + ", time="
            + time
            + ", amount="
            + amount
            + ", amountThreshold="
            + amountThreshold
            + ", startTime="
            + startTime
            + ", endTime="
            + endTime
            + ", ratioThreshold="
            + ratioThreshold
            + '}';
    }
}

