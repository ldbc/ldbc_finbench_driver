package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead2Result {
    public static final String OTHER_ID = "otherId";
    public static final String SUM_LOAN_AMOUNT = "sumLoanAmount";
    public static final String SUM_LOAN_BALANCE = "sumLoanBalance";
    private final long otherId;
    private final long sumLoanAmount;
    private final long sumLoanBalance;

    public ComplexRead2Result(@JsonProperty(OTHER_ID) long otherId,
                              @JsonProperty(SUM_LOAN_AMOUNT) long sumLoanAmount,
                              @JsonProperty(SUM_LOAN_BALANCE) long sumLoanBalance) {
        this.otherId = otherId;
        this.sumLoanAmount = sumLoanAmount;
        this.sumLoanBalance = sumLoanBalance;
    }

    public long getOtherId() {
        return otherId;
    }

    public long getSumLoanAmount() {
        return sumLoanAmount;
    }

    public long getSumLoanBalance() {
        return sumLoanBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead2Result that = (ComplexRead2Result) o;
        return otherId == that.otherId
            && sumLoanAmount == that.sumLoanAmount
            && sumLoanBalance == that.sumLoanBalance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(otherId, sumLoanAmount, sumLoanBalance);
    }

    @Override
    public String toString() {
        return "ComplexRead2Result{"
            + "otherId="
            + otherId
            + ", sumLoanAmount="
            + sumLoanAmount
            + ", sumLoanBalance="
            + sumLoanBalance
            + '}';
    }
}

