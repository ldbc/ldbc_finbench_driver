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
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;

public class ReadWrite2 extends Operation<LdbcNoResult> {
    public static final int TYPE = 10002;
    public static final String SRC_ID = "srcId";
    public static final String DST_ID = "dstId";
    public static final String CURRENT_TIME = "currentTime";
    public static final String AMT = "amt";
    public static final String THRESHOLD = "threshold";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    private final long srcId;
    private final long dstId;
    private final Date currentTime;
    private final long amt;
    private final long threshold;
    private final Date startTime;
    private final Date endTime;

    public ReadWrite2(@JsonProperty(SRC_ID) long srcId,
                      @JsonProperty(DST_ID) long dstId,
                      @JsonProperty(CURRENT_TIME) Date currentTime,
                      @JsonProperty(AMT) long amt,
                      @JsonProperty(THRESHOLD) long threshold,
                      @JsonProperty(START_TIME) Date startTime,
                      @JsonProperty(END_TIME) Date endTime) {
        this.srcId = srcId;
        this.dstId = dstId;
        this.currentTime = currentTime;
        this.amt = amt;
        this.threshold = threshold;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getSrcId() {
        return srcId;
    }

    public long getDstId() {
        return dstId;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public long getAmt() {
        return amt;
    }

    public long getThreshold() {
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
            .put(SRC_ID, srcId)
            .put(DST_ID, dstId)
            .put(CURRENT_TIME, currentTime)
            .put(AMT, amt)
            .put(THRESHOLD, threshold)
            .put(START_TIME, startTime)
            .put(END_TIME, endTime)
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
            && Objects.equals(currentTime, that.currentTime)
            && amt == that.amt
            && threshold == that.threshold
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcId, dstId, currentTime, amt, threshold, startTime, endTime);
    }

    @Override
    public String toString() {
        return "ReadWrite2{"
            + "srcId="
            + srcId
            + ", dstId="
            + dstId
            + ", currentTime="
            + currentTime
            + ", amt="
            + amt
            + ", threshold="
            + threshold
            + ", startTime="
            + startTime
            + ", endTime="
            + endTime
            + '}';
    }
}

