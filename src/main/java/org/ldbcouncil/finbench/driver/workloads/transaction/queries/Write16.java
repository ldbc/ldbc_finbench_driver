package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 16:
 * -- Account signed in with Medium --
 *  Add a *signIn* edge from medium to an *Account* node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write16 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1016;
    public static final String MEDIUM_ID = "mediumId";
    public static final String ACCOUNT_ID = "accountId";
    public static final String TIME = "time";
    private final long mediumId;
    private final long accountId;
    private final Date time;

    public Write16(@JsonProperty(MEDIUM_ID) long mediumId,
                   @JsonProperty(ACCOUNT_ID) long accountId,
                   @JsonProperty(TIME) Date time) {
        this.mediumId = mediumId;
        this.accountId = accountId;
        this.time = time;
    }

    public Write16(Write16 operation) {
        this.mediumId = operation.mediumId;
        this.accountId = operation.accountId;
        this.time = operation.time;
    }

    @Override
    public Write16 newInstance() {
        return new Write16(this);
    }

    public long getMediumId() {
        return mediumId;
    }

    public long getAccountId() {
        return accountId;
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
            .put(MEDIUM_ID, mediumId)
            .put(ACCOUNT_ID, accountId)
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
        Write16 that = (Write16) o;
        return mediumId == that.mediumId
            && accountId == that.accountId
            && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mediumId, accountId, time);
    }

    @Override
    public String toString() {
        return "Write16{"
            + "mediumId="
            + mediumId
            + ", accountId="
            + accountId
            + ", time="
            + time
            + '}';
    }
}

