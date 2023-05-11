package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 8:
 * -- Loan Deposit --
 * Add a deposit edge from a Loan node to an existed Account node. Note that the deposit from
the loan to the account is one-off.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write8 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1008;
    public static final String ACCOUNT_ID = "accountId";
    public static final String LOAN_ID = "loanId";
    public static final String TIME = "time";
    public static final String AMOUNT = "amount";
    private final long accountId;
    private final long loanId;
    private final Date time;
    private final long amount;

    public Write8(@JsonProperty(ACCOUNT_ID) long accountId,
                  @JsonProperty(LOAN_ID) long loanId,
                  @JsonProperty(TIME) Date time,
                  @JsonProperty(AMOUNT) long amount) {
        this.accountId = accountId;
        this.loanId = loanId;
        this.time = time;
        this.amount = amount;
    }

    public Write8(Write8 operation) {
        this.accountId = operation.accountId;
        this.loanId = operation.loanId;
        this.time = operation.time;
        this.amount = operation.amount;
    }

    @Override
    public Write8 newInstance() {
        return new Write8(this);
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
        Write8 that = (Write8) o;
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
        return "Write8{"
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

