package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload write query 5:
 * -- Add Loan applied by Person --
 * Add a Loan Node and add an apply edge from an existed person node to it.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.Operation;

public class Write5 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1005;
    public static final String LOAN_ID = "loanId";
    public static final String LOAN_AMOUNT = "loanAmount";
    public static final String PERSON_ID = "personId";
    public static final String CURRENT_TIME = "currentTime";
    private final long loanId;
    private final long loanAmount;
    private final long personId;
    private final Date currentTime;

    public Write5(@JsonProperty(LOAN_ID) long loanId,
                  @JsonProperty(LOAN_AMOUNT) long loanAmount,
                  @JsonProperty(PERSON_ID) long personId,
                  @JsonProperty(CURRENT_TIME) Date currentTime) {
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.personId = personId;
        this.currentTime = currentTime;
    }

    public long getLoanId() {
        return loanId;
    }

    public long getLoanAmount() {
        return loanAmount;
    }

    public long getPersonId() {
        return personId;
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
            .put(PERSON_ID, personId)
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
        Write5 that = (Write5) o;
        return loanId == that.loanId
            && loanAmount == that.loanAmount
            && personId == that.personId
            && Objects.equals(currentTime, that.currentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId, loanAmount, personId, currentTime);
    }

    @Override
    public String toString() {
        return "Write5{"
            + "loanId="
            + loanId
            + ", loanAmount="
            + loanAmount
            + ", personId="
            + personId
            + ", currentTime="
            + currentTime
            + '}';
    }
}

