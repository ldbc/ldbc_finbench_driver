package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead12Result {
    public static final String SUM_LOAN_AMOUNT = "sumLoanAmount";
    public static final String COUNT_LOAN = "countLoan";
    private final long sumLoanAmount;
    private final int countLoan;

    public ComplexRead12Result(@JsonProperty(SUM_LOAN_AMOUNT) long sumLoanAmount,
                               @JsonProperty(COUNT_LOAN) int countLoan) {
        this.sumLoanAmount = sumLoanAmount;
        this.countLoan = countLoan;
    }

    public long getSumLoanAmount() {
        return sumLoanAmount;
    }

    public int getCountLoan() {
        return countLoan;
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
            && countLoan == that.countLoan;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sumLoanAmount, countLoan);
    }

    @Override
    public String toString() {
        return "ComplexRead12Result{"
            + "sumLoanAmount="
            + sumLoanAmount
            + ", countLoan="
            + countLoan
            + '}';
    }
}

