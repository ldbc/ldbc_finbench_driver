package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload write query 13:
 * -- Add guarantee between persons --
 * Add a guarantee edge from an existed person node to another existed person node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.Operation;

public class Write13 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1013;
    public static final String PID1 = "pid1";
    public static final String PID2 = "pid2";
    public static final String CURRENT_TIME = "currentTime";
    private final long pid1;
    private final long pid2;
    private final Date currentTime;

    public Write13(@JsonProperty(PID1) long pid1,
                   @JsonProperty(PID2) long pid2,
                   @JsonProperty(CURRENT_TIME) Date currentTime) {
        this.pid1 = pid1;
        this.pid2 = pid2;
        this.currentTime = currentTime;
    }

    public long getPid1() {
        return pid1;
    }

    public long getPid2() {
        return pid2;
    }

    public Date getCurrentTime() {
        return currentTime;
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
            .put(CURRENT_TIME, currentTime)
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
        Write13 that = (Write13) o;
        return pid1 == that.pid1
            && pid2 == that.pid2
            && Objects.equals(currentTime, that.currentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid1, pid2, currentTime);
    }

    @Override
    public String toString() {
        return "Write13{"
            + "pid1="
            + pid1
            + ", pid2="
            + pid2
            + ", currentTime="
            + currentTime
            + '}';
    }
}

