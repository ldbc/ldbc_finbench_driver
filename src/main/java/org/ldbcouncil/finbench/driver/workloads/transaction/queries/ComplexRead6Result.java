package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead6Result {
    public static final String MID_ID = "midId";
    public static final String SUM_EDGE1_AMOUNT = "sumEdge1Amount";
    public static final String SUM_EDGE2_AMOUNT = "sumEdge2Amount";
    private final long midId;
    private final double sumEdge1Amount;
    private final double sumEdge2Amount;

    public ComplexRead6Result(@JsonProperty(MID_ID) long midId,
                              @JsonProperty(SUM_EDGE1_AMOUNT) double sumEdge1Amount,
                              @JsonProperty(SUM_EDGE2_AMOUNT) double sumEdge2Amount) {
        this.midId = midId;
        this.sumEdge1Amount = sumEdge1Amount;
        this.sumEdge2Amount = sumEdge2Amount;
    }

    public long getMidId() {
        return midId;
    }

    public double getSumEdge1Amount() {
        return sumEdge1Amount;
    }

    public double getSumEdge2Amount() {
        return sumEdge2Amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead6Result that = (ComplexRead6Result) o;
        return midId == that.midId
            && sumEdge1Amount == that.sumEdge1Amount
            && sumEdge2Amount == that.sumEdge2Amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(midId, sumEdge1Amount, sumEdge2Amount);
    }

    @Override
    public String toString() {
        return "ComplexRead6Result{"
            + "midId="
            + midId
            + ", sumEdge1Amount="
            + sumEdge1Amount
            + ", sumEdge2Amount="
            + sumEdge2Amount
            + '}';
    }
}

