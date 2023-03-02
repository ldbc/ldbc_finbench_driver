package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 7:
 * -- Account signed in with Medium --
 * Add an Medium node and add a signIn edge from it to an existed Account node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;

public class Write7 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1007;
    public static final String ACCOUNT_ID = "accountId";
    public static final String MEDIUM_ID = "mediumId";
    public static final String MEDIUM_BLOCKED = "mediumBlocked";
    public static final String CURRENT_TIME = "currentTime";
    private final long accountId;
    private final long mediumId;
    private final boolean mediumBlocked;
    private final Date currentTime;

    public Write7(@JsonProperty(ACCOUNT_ID) long accountId,
                  @JsonProperty(MEDIUM_ID) long mediumId,
                  @JsonProperty(MEDIUM_BLOCKED) boolean mediumBlocked,
                  @JsonProperty(CURRENT_TIME) Date currentTime) {
        this.accountId = accountId;
        this.mediumId = mediumId;
        this.mediumBlocked = mediumBlocked;
        this.currentTime = currentTime;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getMediumId() {
        return mediumId;
    }

    public boolean getMediumBlocked() {
        return mediumBlocked;
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
            .put(ACCOUNT_ID, accountId)
            .put(MEDIUM_ID, mediumId)
            .put(MEDIUM_BLOCKED, mediumBlocked)
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
        Write7 that = (Write7) o;
        return accountId == that.accountId
            && mediumId == that.mediumId
            && mediumBlocked == that.mediumBlocked
            && Objects.equals(currentTime, that.currentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, mediumId, mediumBlocked, currentTime);
    }

    @Override
    public String toString() {
        return "Write7{"
            + "accountId="
            + accountId
            + ", mediumId="
            + mediumId
            + ", mediumBlocked="
            + mediumBlocked
            + ", currentTime="
            + currentTime
            + '}';
    }
}

