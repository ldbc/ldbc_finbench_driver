package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload read write query 3:
 * -- Guarantee under guarantee chain detection strategy --
 * The workflow of this read write query contains at least one transaction. It works as:
• In the very beginning, read the blocked status of related persons. The transaction aborts
if one of them is blocked. Move to the next step if none is blocked.
• Add a guarantee edge between the two persons inside the transaction.
• Detect if a guarantee chain pattern formed. Calculate if the amount sum of the related
loans in the chain exceeds a given threshold. Transaction aborts if the sum exceeds the
threshold, and then mark the related persons as blocked in another transaction. Otherwise
the transaction commits.

 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;

public class ReadWrite3 extends Operation<LdbcNoResult> {
    public static final int TYPE = 10003;
    public static final String SRC_ID = "srcId";
    public static final String DST_ID = "dstId";
    public static final String THRESHOLD = "threshold";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    private final long srcId;
    private final long dstId;
    private final long threshold;
    private final Date startTime;
    private final Date endTime;

    public ReadWrite3(@JsonProperty(SRC_ID) long srcId,
                      @JsonProperty(DST_ID) long dstId,
                      @JsonProperty(THRESHOLD) long threshold,
                      @JsonProperty(START_TIME) Date startTime,
                      @JsonProperty(END_TIME) Date endTime) {
        this.srcId = srcId;
        this.dstId = dstId;
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
        ReadWrite3 that = (ReadWrite3) o;
        return srcId == that.srcId
            && dstId == that.dstId
            && threshold == that.threshold
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcId, dstId, threshold, startTime, endTime);
    }

    @Override
    public String toString() {
        return "ReadWrite3{"
            + "srcId="
            + srcId
            + ", dstId="
            + dstId
            + ", threshold="
            + threshold
            + ", startTime="
            + startTime
            + ", endTime="
            + endTime
            + '}';
    }
}

