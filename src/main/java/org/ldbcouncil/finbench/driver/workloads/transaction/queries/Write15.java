package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 15:
 * -- Add Deposit Between Loan And Account --
 * Add a *deposit* edge from a *Loan* node to an *Account* node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write15 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1015;
    public static final String LOAN_ID = "loanId";
    public static final String ACCOUNT_ID = "accountId";
    public static final String TIME = "time";
    public static final String AMOUNT = "amount";
    private final long loanId;
    private final long accountId;
    private final Date time;
    private final double amount;

    public Write15(@JsonProperty(LOAN_ID) long loanId,
                   @JsonProperty(ACCOUNT_ID) long accountId,
                   @JsonProperty(TIME) Date time,
                   @JsonProperty(AMOUNT) double amount) {
        this.loanId = loanId;
        this.accountId = accountId;
        this.time = time;
        this.amount = amount;
    }

    public Write15(Write15 operation) {
        this.loanId = operation.loanId;
        this.accountId = operation.accountId;
        this.time = operation.time;
        this.amount = operation.amount;
    }

    @Override
    public Write15 newInstance() {
        return new Write15(this);
    }

    public long getLoanId() {
        return loanId;
    }

    public long getAccountId() {
        return accountId;
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
            .put(LOAN_ID, loanId)
            .put(ACCOUNT_ID, accountId)
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
        Write15 that = (Write15) o;
        return loanId == that.loanId
            && accountId == that.accountId
            && Objects.equals(time, that.time)
            && amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId, accountId, time, amount);
    }

    @Override
    public String toString() {
        return "Write15{"
            + "loanId="
            + loanId
            + ", accountId="
            + accountId
            + ", time="
            + time
            + ", amount="
            + amount
            + '}';
    }
}

