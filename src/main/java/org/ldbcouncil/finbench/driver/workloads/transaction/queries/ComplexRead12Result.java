package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead12Result {
    public static final String SUM_LOAN_AMOUNT = "sumLoanAmount";
    public static final String NUM_LOANS = "numLoans";
    private final long sumLoanAmount;
    private final int numLoans;

    public ComplexRead12Result(@JsonProperty(SUM_LOAN_AMOUNT) long sumLoanAmount,
                               @JsonProperty(NUM_LOANS) int numLoans) {
        this.sumLoanAmount = sumLoanAmount;
        this.numLoans = numLoans;
    }

    public long getSumLoanAmount() {
        return sumLoanAmount;
    }

    public int getNumLoans() {
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
        ComplexRead12Result that = (ComplexRead12Result) o;
        return sumLoanAmount == that.sumLoanAmount
            && numLoans == that.numLoans;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sumLoanAmount, numLoans);
    }

    @Override
    public String toString() {
        return "ComplexRead12Result{"
            + "sumLoanAmount="
            + sumLoanAmount
            + ", numLoans="
            + numLoans
            + '}';
    }
}

