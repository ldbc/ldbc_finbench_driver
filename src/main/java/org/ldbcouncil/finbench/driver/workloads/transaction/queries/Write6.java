package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 6:
 * -- Add Loan applied by Company --
 * Add a Loan Node and add an apply edge from an existed company node to it.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write6 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1006;
    public static final String COMPANY_ID = "companyId";
    public static final String TIME = "time";
    public static final String LOAN_ID = "loanId";
    public static final String AMOUNT = "amount";
    private final long companyId;
    private final Date time;
    private final long loanId;
    private final long amount;

    public Write6(@JsonProperty(COMPANY_ID) long companyId,
                  @JsonProperty(TIME) Date time,
                  @JsonProperty(LOAN_ID) long loanId,
                  @JsonProperty(AMOUNT) long amount) {
        this.companyId = companyId;
        this.time = time;
        this.loanId = loanId;
        this.amount = amount;
    }

    public long getCompanyId() {
        return companyId;
    }

    public Date getTime() {
        return time;
    }

    public long getLoanId() {
        return loanId;
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
            .put(COMPANY_ID, companyId)
            .put(TIME, time)
            .put(LOAN_ID, loanId)
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
        Write6 that = (Write6) o;
        return companyId == that.companyId
            && Objects.equals(time, that.time)
            && loanId == that.loanId
            && amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, time, loanId, amount);
    }

    @Override
    public String toString() {
        return "Write6{"
            + "companyId="
            + companyId
            + ", time="
            + time
            + ", loanId="
            + loanId
            + ", amount="
            + amount
            + '}';
    }
}

