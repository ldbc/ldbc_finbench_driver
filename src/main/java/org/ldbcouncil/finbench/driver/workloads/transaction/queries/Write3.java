package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 3:
 * -- Add transfer between accounts --
 * Add a transfer edge from an existed account node to another existed account node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;

public class Write3 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1003;
    public static final String SRC_ID = "srcId";
    public static final String DST_ID = "dstId";
    public static final String TIME = "time";
    public static final String AMOUNT = "amount";
    private final long srcId;
    private final long dstId;
    private final Date time;
    private final long amount;

    public Write3(@JsonProperty(SRC_ID) long srcId,
                  @JsonProperty(DST_ID) long dstId,
                  @JsonProperty(TIME) Date time,
                  @JsonProperty(AMOUNT) long amount) {
        this.srcId = srcId;
        this.dstId = dstId;
        this.time = time;
        this.amount = amount;
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
        Write3 that = (Write3) o;
        return srcId == that.srcId
            && dstId == that.dstId
            && Objects.equals(time, that.time)
            && amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcId, dstId, time, amount);
    }

    @Override
    public String toString() {
        return "Write3{"
            + "srcId="
            + srcId
            + ", dstId="
            + dstId
            + ", time="
            + time
            + ", amount="
            + amount
            + '}';
    }
}

