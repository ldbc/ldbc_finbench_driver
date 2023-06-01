package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 6:
 * -- Add Loan applied by Person --
 * Add a *Loan* node and add an *apply* edge from *Person* node to *Loan* node.
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
    public static final String PERSON_ID = "personId";
    public static final String LOAN_ID = "loanId";
    public static final String LOAN_AMOUNT = "loanAmount";
    public static final String BALANCE = "balance";
    public static final String TIME = "time";
    private final long personId;
    private final long loanId;
    private final double loanAmount;
    private final double balance;
    private final Date time;

    public Write6(@JsonProperty(PERSON_ID) long personId,
                  @JsonProperty(LOAN_ID) long loanId,
                  @JsonProperty(LOAN_AMOUNT) double loanAmount,
                  @JsonProperty(BALANCE) double balance,
                  @JsonProperty(TIME) Date time) {
        this.personId = personId;
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.balance = balance;
        this.time = time;
    }

    public Write6(Write6 operation) {
        this.personId = operation.personId;
        this.loanId = operation.loanId;
        this.loanAmount = operation.loanAmount;
        this.balance = operation.balance;
        this.time = operation.time;
    }

    @Override
    public Write6 newInstance() {
        return new Write6(this);
    }

    public long getPersonId() {
        return personId;
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
            .put(PERSON_ID, personId)
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
        Write6 that = (Write6) o;
        return personId == that.personId
            && loanId == that.loanId
            && loanAmount == that.loanAmount
            && balance == that.balance
            && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, loanId, loanAmount, balance, time);
    }

    @Override
    public String toString() {
        return "Write6{"
            + "personId="
            + personId
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

