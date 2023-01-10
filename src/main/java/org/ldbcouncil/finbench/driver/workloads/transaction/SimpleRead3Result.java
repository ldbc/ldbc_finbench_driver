package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead3Result {
    public static final String ACCOUNT_ID = "accountId";
    public static final String INVEST_PERSON_ID = "investPersonId";
    public static final String WORKER_ID = "workerId";
    public static final String INVEST_COMPANY_ID = "investCompanyId";
    public static final String LOAN_ID = "loanId";
    private final long accountId;
    private final long investPersonId;
    private final long workerId;
    private final long investCompanyId;
    private final long loanId;

    public SimpleRead3Result(@JsonProperty(ACCOUNT_ID) long accountId,
                             @JsonProperty(INVEST_PERSON_ID) long investPersonId,
                             @JsonProperty(WORKER_ID) long workerId,
                             @JsonProperty(INVEST_COMPANY_ID) long investCompanyId,
                             @JsonProperty(LOAN_ID) long loanId) {
        this.accountId = accountId;
        this.investPersonId = investPersonId;
        this.workerId = workerId;
        this.investCompanyId = investCompanyId;
        this.loanId = loanId;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getInvestPersonId() {
        return investPersonId;
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getInvestCompanyId() {
        return investCompanyId;
    }

    public long getLoanId() {
        return loanId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead3Result that = (SimpleRead3Result) o;
        return accountId == that.accountId
            && investPersonId == that.investPersonId
            && workerId == that.workerId
            && investCompanyId == that.investCompanyId
            && loanId == that.loanId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, investPersonId, workerId, investCompanyId, loanId);
    }

    @Override
    public String toString() {
        return "SimpleRead3Result{"
            + "accountId="
            + accountId
            + ", investPersonId="
            + investPersonId
            + ", workerId="
            + workerId
            + ", investCompanyId="
            + investCompanyId
            + ", loanId="
            + loanId
            + '}';
    }
}

