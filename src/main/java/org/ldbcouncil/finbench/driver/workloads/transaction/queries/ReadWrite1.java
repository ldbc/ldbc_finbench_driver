package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload read write query 1:
 * -- Transfer under transfer cycle detection strategy --
 * The workflow of this read write query contains at least one transaction. It works as:
• In the very beginning, read the blocked status of related accounts with given ids of two src
and dst accounts. The transaction aborts if one of them is blocked. Move to the next step
if none is blocked.
• Add a transfer edge from src to dst inside a transaction. Given a specified time window
between startTime and endTime, find the other accounts which received money from dst and
transferred money to src in a specific time. Transaction aborts if a new transfer cycle is
formed, otherwise the transaction commits.
• If the last transaction aborts, mark the src and dst accounts as blocked in another transaction.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class ReadWrite1 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 10001;
    public static final String SRC_ID = "srcId";
    public static final String DST_ID = "dstId";
    public static final String TIME = "time";
    public static final String AMOUNT = "amount";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    private final long srcId;
    private final long dstId;
    private final Date time;
    private final double amount;
    private final Date startTime;
    private final Date endTime;

    public ReadWrite1(@JsonProperty(SRC_ID) long srcId,
                      @JsonProperty(DST_ID) long dstId,
                      @JsonProperty(TIME) Date time,
                      @JsonProperty(AMOUNT) double amount,
                      @JsonProperty(START_TIME) Date startTime,
                      @JsonProperty(END_TIME) Date endTime) {
        this.srcId = srcId;
        this.dstId = dstId;
        this.time = time;
        this.amount = amount;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ReadWrite1(ReadWrite1 operation) {
        this.srcId = operation.srcId;
        this.dstId = operation.dstId;
        this.time = operation.time;
        this.amount = operation.amount;
        this.startTime = operation.startTime;
        this.endTime = operation.endTime;
    }

    @Override
    public ReadWrite1 newInstance() {
        return new ReadWrite1(this);
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

    public double getAmount() {
        return amount;
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
            .put(TIME, time)
            .put(AMOUNT, amount)
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
        ReadWrite1 that = (ReadWrite1) o;
        return srcId == that.srcId
            && dstId == that.dstId
            && Objects.equals(time, that.time)
            && amount == that.amount
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcId, dstId, time, amount, startTime, endTime);
    }

    @Override
    public String toString() {
        return "ReadWrite1{"
            + "srcId="
            + srcId
            + ", dstId="
            + dstId
            + ", time="
            + time
            + ", amount="
            + amount
            + ", startTime="
            + startTime
            + ", endTime="
            + endTime
            + '}';
    }
}

