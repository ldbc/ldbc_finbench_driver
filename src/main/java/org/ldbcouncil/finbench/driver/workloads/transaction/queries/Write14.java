package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 14:
 * -- Add Repay Between Account And Loan --
 * AAdd a *repay* edge from an *Account* node to a *Loan* node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write14 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1014;
    public static final String ACCOUNT_ID = "accountId";
    public static final String LOAN_ID = "loanId";
    public static final String TIME = "time";
    public static final String AMOUNT = "amount";
    private final long accountId;
    private final long loanId;
    private final Date time;
    private final double amount;

    public Write14(@JsonProperty(ACCOUNT_ID) long accountId,
                   @JsonProperty(LOAN_ID) long loanId,
                   @JsonProperty(TIME) Date time,
                   @JsonProperty(AMOUNT) double amount) {
        this.accountId = accountId;
        this.loanId = loanId;
        this.time = time;
        this.amount = amount;
    }

    public Write14(Write14 operation) {
        this.accountId = operation.accountId;
        this.loanId = operation.loanId;
        this.time = operation.time;
        this.amount = operation.amount;
    }

    @Override
    public Write14 newInstance() {
        return new Write14(this);
    }

    public long getAccountId() {
        return accountId;
    }

    public long getLoanId() {
        return loanId;
    }

    public Date getTime() {
        return time;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(ACCOUNT_ID, accountId)
            .put(LOAN_ID, loanId)
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
        Write14 that = (Write14) o;
        return accountId == that.accountId
            && loanId == that.loanId
            && Objects.equals(time, that.time)
            && amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, loanId, time, amount);
    }

    @Override
    public String toString() {
        return "Write14{"
            + "accountId="
            + accountId
            + ", loanId="
            + loanId
            + ", time="
            + time
            + ", amount="
            + amount
            + '}';
    }
}

