package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead4Result {
    public static final String COUNT_EDGE2 = "countEdge2";
    public static final String SUM_EDGE2_AMOUNT = "sumEdge2Amount";
    public static final String MAX_EDGE2_AMOUNT = "maxEdge2Amount";
    public static final String COUNT_EDGE3 = "countEdge3";
    public static final String SUM_EDGE3_AMOUNT = "sumEdge3Amount";
    public static final String MAX_EDGE3_AMOUNT = "maxEdge3Amount";
    private final long countEdge2;
    private final long sumEdge2Amount;
    private final long maxEdge2Amount;
    private final long countEdge3;
    private final long sumEdge3Amount;
    private final long maxEdge3Amount;

    public ComplexRead4Result(@JsonProperty(COUNT_EDGE2) long countEdge2,
                              @JsonProperty(SUM_EDGE2_AMOUNT) long sumEdge2Amount,
                              @JsonProperty(MAX_EDGE2_AMOUNT) long maxEdge2Amount,
                              @JsonProperty(COUNT_EDGE3) long countEdge3,
                              @JsonProperty(SUM_EDGE3_AMOUNT) long sumEdge3Amount,
                              @JsonProperty(MAX_EDGE3_AMOUNT) long maxEdge3Amount) {
        this.countEdge2 = countEdge2;
        this.sumEdge2Amount = sumEdge2Amount;
        this.maxEdge2Amount = maxEdge2Amount;
        this.countEdge3 = countEdge3;
        this.sumEdge3Amount = sumEdge3Amount;
        this.maxEdge3Amount = maxEdge3Amount;
    }

    public long getCountEdge2() {
        return countEdge2;
    }

    public long getSumEdge2Amount() {
        return sumEdge2Amount;
    }

    public long getMaxEdge2Amount() {
        return maxEdge2Amount;
    }

    public long getCountEdge3() {
        return countEdge3;
    }

    public long getSumEdge3Amount() {
        return sumEdge3Amount;
    }

    public long getMaxEdge3Amount() {
        return maxEdge3Amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead4Result that = (ComplexRead4Result) o;
        return countEdge2 == that.countEdge2
            && sumEdge2Amount == that.sumEdge2Amount
            && maxEdge2Amount == that.maxEdge2Amount
            && countEdge3 == that.countEdge3
            && sumEdge3Amount == that.sumEdge3Amount
            && maxEdge3Amount == that.maxEdge3Amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(countEdge2, sumEdge2Amount, maxEdge2Amount, countEdge3, sumEdge3Amount, maxEdge3Amount);
    }

    @Override
    public String toString() {
        return "ComplexRead4Result{"
            + "countEdge2="
            + countEdge2
            + ", sumEdge2Amount="
            + sumEdge2Amount
            + ", maxEdge2Amount="
            + maxEdge2Amount
            + ", countEdge3="
            + countEdge3
            + ", sumEdge3Amount="
            + sumEdge3Amount
            + ", maxEdge3Amount="
            + maxEdge3Amount
            + '}';
    }
}

