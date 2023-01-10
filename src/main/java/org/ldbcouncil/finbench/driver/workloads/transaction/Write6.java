package org.ldbcouncil.finbench.driver.workloads.transaction;
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
import org.ldbcouncil.finbench.driver.Operation;

public class Write6 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1006;
    public static final String LOAN_ID = "loanId";
    public static final String LOAN_AMOUNT = "loanAmount";
    public static final String COMPANY_ID = "companyId";
    public static final String CURRENT_TIME = "currentTime";
    private final long loanId;
    private final long loanAmount;
    private final long companyId;
    private final Date currentTime;

    public Write6(@JsonProperty(LOAN_ID) long loanId,
                  @JsonProperty(LOAN_AMOUNT) long loanAmount,
                  @JsonProperty(COMPANY_ID) long companyId,
                  @JsonProperty(CURRENT_TIME) Date currentTime) {
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.companyId = companyId;
        this.currentTime = currentTime;
    }

    public long getLoanId() {
        return loanId;
    }

    public long getLoanAmount() {
        return loanAmount;
    }

    public long getCompanyId() {
        return companyId;
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
            .put(LOAN_ID, loanId)
            .put(LOAN_AMOUNT, loanAmount)
            .put(COMPANY_ID, companyId)
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
        Write6 that = (Write6) o;
        return loanId == that.loanId
            && loanAmount == that.loanAmount
            && companyId == that.companyId
            && Objects.equals(currentTime, that.currentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId, loanAmount, companyId, currentTime);
    }

    @Override
    public String toString() {
        return "Write6{"
            + "loanId="
            + loanId
            + ", loanAmount="
            + loanAmount
            + ", companyId="
            + companyId
            + ", currentTime="
            + currentTime
            + '}';
    }
}

