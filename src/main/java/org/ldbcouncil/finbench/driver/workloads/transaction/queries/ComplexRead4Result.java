package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead4Result {
    public static final String OTHER_ID = "otherId";
    public static final String NUM_EDGE2 = "numEdge2";
    public static final String SUM_EDGE2_AMOUNT = "sumEdge2Amount";
    public static final String MAX_EDGE2_AMOUNT = "maxEdge2Amount";
    public static final String NUM_EDGE3 = "numEdge3";
    public static final String SUM_EDGE3_AMOUNT = "sumEdge3Amount";
    public static final String MAX_EDGE3_AMOUNT = "maxEdge3Amount";
    private final long otherId;
    private final long numEdge2;
    private final double sumEdge2Amount;
    private final double maxEdge2Amount;
    private final long numEdge3;
    private final double sumEdge3Amount;
    private final double maxEdge3Amount;

    public ComplexRead4Result(@JsonProperty(OTHER_ID) long otherId,
                              @JsonProperty(NUM_EDGE2) long numEdge2,
                              @JsonProperty(SUM_EDGE2_AMOUNT) double sumEdge2Amount,
                              @JsonProperty(MAX_EDGE2_AMOUNT) double maxEdge2Amount,
                              @JsonProperty(NUM_EDGE3) long numEdge3,
                              @JsonProperty(SUM_EDGE3_AMOUNT) double sumEdge3Amount,
                              @JsonProperty(MAX_EDGE3_AMOUNT) double maxEdge3Amount) {
        this.otherId = otherId;
        this.numEdge2 = numEdge2;
        this.sumEdge2Amount = sumEdge2Amount;
        this.maxEdge2Amount = maxEdge2Amount;
        this.numEdge3 = numEdge3;
        this.sumEdge3Amount = sumEdge3Amount;
        this.maxEdge3Amount = maxEdge3Amount;
    }

    public long getOtherId() {
        return otherId;
    }

    public long getNumEdge2() {
        return numEdge2;
    }

    public double getSumEdge2Amount() {
        return sumEdge2Amount;
    }

    public double getMaxEdge2Amount() {
        return maxEdge2Amount;
    }

    public long getNumEdge3() {
        return numEdge3;
    }

    public double getSumEdge3Amount() {
        return sumEdge3Amount;
    }

    public double getMaxEdge3Amount() {
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
        return otherId == that.otherId
            && numEdge2 == that.numEdge2
            && sumEdge2Amount == that.sumEdge2Amount
            && maxEdge2Amount == that.maxEdge2Amount
            && numEdge3 == that.numEdge3
            && sumEdge3Amount == that.sumEdge3Amount
            && maxEdge3Amount == that.maxEdge3Amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(otherId, numEdge2, sumEdge2Amount, maxEdge2Amount,
            numEdge3, sumEdge3Amount, maxEdge3Amount);
    }

    @Override
    public String toString() {
        return "ComplexRead4Result{"
            + "otherId="
            + otherId
            + ", numEdge2="
            + numEdge2
            + ", sumEdge2Amount="
            + sumEdge2Amount
            + ", maxEdge2Amount="
            + maxEdge2Amount
            + ", numEdge3="
            + numEdge3
            + ", sumEdge3Amount="
            + sumEdge3Amount
            + ", maxEdge3Amount="
            + maxEdge3Amount
            + '}';
    }
}

