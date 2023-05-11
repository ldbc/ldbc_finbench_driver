package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 12:
 * -- Add guarantee between persons --
 * Add a guarantee edge from an existed person node to another existed person node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write12 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1012;
    public static final String PID1 = "pid1";
    public static final String PID2 = "pid2";
    public static final String TIME = "time";
    private final long pid1;
    private final long pid2;
    private final Date time;

    public Write12(@JsonProperty(PID1) long pid1,
                   @JsonProperty(PID2) long pid2,
                   @JsonProperty(TIME) Date time) {
        this.pid1 = pid1;
        this.pid2 = pid2;
        this.time = time;
    }

    public Write12(Write12 operation) {
        this.pid1 = operation.pid1;
        this.pid2 = operation.pid2;
        this.time = operation.time;
    }

    @Override
    public Write12 newInstance() {
        return new Write12(this);
    }

    public long getPid1() {
        return pid1;
    }

    public long getPid2() {
        return pid2;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(PID1, pid1)
            .put(PID2, pid2)
            .put(TIME, time)
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
        Write12 that = (Write12) o;
        return pid1 == that.pid1
            && pid2 == that.pid2
            && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid1, pid2, time);
    }

    @Override
    public String toString() {
        return "Write12{"
            + "pid1="
            + pid1
            + ", pid2="
            + pid2
            + ", time="
            + time
            + '}';
    }
}

