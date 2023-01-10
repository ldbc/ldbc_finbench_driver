package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead2Result {
    public static final String SUM_LOAN_AMOUNT = "sumLoanAmount";
    public static final String SUM_LOAN_BALANCE = "sumLoanBalance";
    public static final String COUNT_LOAN = "countLoan";
    private final long sumLoanAmount;
    private final long sumLoanBalance;
    private final long countLoan;

    public ComplexRead2Result(@JsonProperty(SUM_LOAN_AMOUNT) long sumLoanAmount,
                              @JsonProperty(SUM_LOAN_BALANCE) long sumLoanBalance,
                              @JsonProperty(COUNT_LOAN) long countLoan) {
        this.sumLoanAmount = sumLoanAmount;
        this.sumLoanBalance = sumLoanBalance;
        this.countLoan = countLoan;
    }

    public long getSumLoanAmount() {
        return sumLoanAmount;
    }

    public long getSumLoanBalance() {
        return sumLoanBalance;
    }

    public long getCountLoan() {
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
        ComplexRead2Result that = (ComplexRead2Result) o;
        return sumLoanAmount == that.sumLoanAmount
            && sumLoanBalance == that.sumLoanBalance
            && countLoan == that.countLoan;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sumLoanAmount, sumLoanBalance, countLoan);
    }

    @Override
    public String toString() {
        return "ComplexRead2Result{"
            + "sumLoanAmount="
            + sumLoanAmount
            + ", sumLoanBalance="
            + sumLoanBalance
            + ", countLoan="
            + countLoan
            + '}';
    }
}

