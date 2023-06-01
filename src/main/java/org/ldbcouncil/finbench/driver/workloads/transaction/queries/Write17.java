package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 17:
 * -- Remove an Account --
 *  Given an id, remove the *Account*, and remove the related edges including
  *own*, *transfer*, *withdraw*, *repay*, *deposit*, *signIn*. Remove the
  connected *Loan* vertex in cascade.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write17 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1017;
    public static final String ACCOUNT_ID = "accountId";
    private final long accountId;

    public Write17(@JsonProperty(ACCOUNT_ID) long accountId) {
        this.accountId = accountId;
    }

    public Write17(Write17 operation) {
        this.accountId = operation.accountId;
    }

    @Override
    public Write17 newInstance() {
        return new Write17(this);
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
        Write17 that = (Write17) o;
        return accountId == that.accountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    @Override
    public String toString() {
        return "Write17{"
            + "accountId="
            + accountId
            + '}';
    }
}

