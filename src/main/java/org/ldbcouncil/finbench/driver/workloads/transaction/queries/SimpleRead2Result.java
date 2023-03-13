package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead2Result {
    public static final String SUM_E1_AMOUNT = "sumE1Amount";
    public static final String MAX_E1_AMOUNT = "maxE1Amount";
    public static final String SUM_E2_AMOUNT = "sumE2Amount";
    public static final String MAX_E2_AMOUNT = "maxE2Amount";
    private final long sumE1Amount;
    private final long maxE1Amount;
    private final long sumE2Amount;
    private final long maxE2Amount;

    public SimpleRead2Result(@JsonProperty(SUM_E1_AMOUNT) long sumE1Amount,
                             @JsonProperty(MAX_E1_AMOUNT) long maxE1Amount,
                             @JsonProperty(SUM_E2_AMOUNT) long sumE2Amount,
                             @JsonProperty(MAX_E2_AMOUNT) long maxE2Amount) {
        this.sumE1Amount = sumE1Amount;
        this.maxE1Amount = maxE1Amount;
        this.sumE2Amount = sumE2Amount;
        this.maxE2Amount = maxE2Amount;
    }

    public long getSumE1Amount() {
        return sumE1Amount;
    }

    public long getMaxE1Amount() {
        return maxE1Amount;
    }

    public long getSumE2Amount() {
        return sumE2Amount;
    }

    public long getMaxE2Amount() {
        return maxE2Amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead2Result that = (SimpleRead2Result) o;
        return sumE1Amount == that.sumE1Amount
            && maxE1Amount == that.maxE1Amount
            && sumE2Amount == that.sumE2Amount
            && maxE2Amount == that.maxE2Amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sumE1Amount, maxE1Amount, sumE2Amount, maxE2Amount);
    }

    @Override
    public String toString() {
        return "SimpleRead2Result{"
            + "sumE1Amount="
            + sumE1Amount
            + ", maxE1Amount="
            + maxE1Amount
            + ", sumE2Amount="
            + sumE2Amount
            + ", maxE2Amount="
            + maxE2Amount
            + '}';
    }
}

