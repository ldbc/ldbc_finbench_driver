package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
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
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;

public class Write5 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1005;
    public static final String PERSON_ID = "personId";
    public static final String TIME = "time";
    public static final String LOAN_ID = "loanId";
    public static final String AMOUNT = "amount";
    private final long personId;
    private final Date time;
    private final long loanId;
    private final long amount;

    public Write5(@JsonProperty(PERSON_ID) long personId,
                  @JsonProperty(TIME) Date time,
                  @JsonProperty(LOAN_ID) long loanId,
                  @JsonProperty(AMOUNT) long amount) {
        this.personId = personId;
        this.time = time;
        this.loanId = loanId;
        this.amount = amount;
    }

    public long getPersonId() {
        return personId;
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
            .put(PERSON_ID, personId)
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
        Write5 that = (Write5) o;
        return personId == that.personId
            && Objects.equals(time, that.time)
            && loanId == that.loanId
            && amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, time, loanId, amount);
    }

    @Override
    public String toString() {
        return "Write5{"
            + "personId="
            + personId
            + ", time="
            + time
            + ", loanId="
            + loanId
            + ", amount="
            + amount
            + '}';
    }
}

