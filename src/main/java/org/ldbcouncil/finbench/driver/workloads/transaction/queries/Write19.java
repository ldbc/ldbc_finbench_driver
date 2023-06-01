package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 19:
 * -- Block a Account of high risk --
 *  Set an *Account*'s isBlocked to True.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write19 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1018;
    public static final String ACCOUNT_ID = "accountId";
    private final long accountId;

    public Write19(@JsonProperty(ACCOUNT_ID) long accountId) {
        this.accountId = accountId;
    }

    public Write19(Write19 operation) {
        this.accountId = operation.accountId;
    }

    @Override
    public Write19 newInstance() {
        return new Write19(this);
    }

    public long getAccountId() {
        return accountId;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(ACCOUNT_ID, accountId)
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
        Write19 that = (Write19) o;
        return accountId == that.accountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    @Override
    public String toString() {
        return "Write19{"
            + "accountId="
            + accountId
            + '}';
    }
}

