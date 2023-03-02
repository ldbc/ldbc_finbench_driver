package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead2Result {
    public static final String SUM_LOAN_AMOUNT = "sumLoanAmount";
    public static final String SUM_LOAN_BALANCE = "sumLoanBalance";
    public static final String NUM_LOANS = "numLoans";
    private final long sumLoanAmount;
    private final long sumLoanBalance;
    private final long numLoans;

    public ComplexRead2Result(@JsonProperty(SUM_LOAN_AMOUNT) long sumLoanAmount,
                              @JsonProperty(SUM_LOAN_BALANCE) long sumLoanBalance,
                              @JsonProperty(NUM_LOANS) long numLoans) {
        this.sumLoanAmount = sumLoanAmount;
        this.sumLoanBalance = sumLoanBalance;
        this.numLoans = numLoans;
    }

    public long getSumLoanAmount() {
        return sumLoanAmount;
    }

    public long getSumLoanBalance() {
        return sumLoanBalance;
    }

    public long getNumLoans() {
        return numLoans;
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
        return sumLoanAmount == that.sumLoanAmount
            && sumLoanBalance == that.sumLoanBalance
            && numLoans == that.numLoans;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sumLoanAmount, sumLoanBalance, numLoans);
    }

    @Override
    public String toString() {
        return "ComplexRead2Result{"
            + "sumLoanAmount="
            + sumLoanAmount
            + ", sumLoanBalance="
            + sumLoanBalance
            + ", numLoans="
            + numLoans
            + '}';
    }
}

