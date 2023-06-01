package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 7:
 * -- Account signed in with Medium --
 * Add Loan applied by Company
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write7 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1007;
    public static final String COMPANY_ID = "companyId";
    public static final String LOAN_ID = "loanId";
    public static final String LOAN_AMOUNT = "loanAmount";
    public static final String BALANCE = "balance";
    public static final String TIME = "time";
    private final long companyId;
    private final long loanId;
    private final double loanAmount;
    private final double balance;
    private final Date time;

    public Write7(@JsonProperty(COMPANY_ID) long companyId,
                  @JsonProperty(LOAN_ID) long loanId,
                  @JsonProperty(LOAN_AMOUNT) double loanAmount,
                  @JsonProperty(BALANCE) double balance,
                  @JsonProperty(TIME) Date time) {
        this.companyId = companyId;
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.balance = balance;
        this.time = time;
    }

    public Write7(Write7 operation) {
        this.companyId = operation.companyId;
        this.loanId = operation.loanId;
        this.loanAmount = operation.loanAmount;
        this.balance = operation.balance;
        this.time = operation.time;
    }

    @Override
    public Write7 newInstance() {
        return new Write7(this);
    }

    public long getCompanyId() {
        return companyId;
    }

    public long getLoanId() {
        return loanId;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getBalance() {
        return balance;
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
            .put(COMPANY_ID, companyId)
            .put(LOAN_ID, loanId)
            .put(LOAN_AMOUNT, loanAmount)
            .put(BALANCE, balance)
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
        Write7 that = (Write7) o;
        return companyId == that.companyId
            && loanId == that.loanId
            && loanAmount == that.loanAmount
            && balance == that.balance
            && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, loanId, loanAmount, balance, time);
    }

    @Override
    public String toString() {
        return "Write7{"
            + "companyId="
            + companyId
            + ", loanId="
            + loanId
            + ", loanAmount="
            + loanAmount
            + ", balance="
            + balance
            + ", time="
            + time
            + '}';
    }
}

