package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead6Result {
    public static final String SUM_EDGE1_AMOUNT = "sumEdge1Amount";
    public static final String SUM_EDGE2_AMOUNT = "sumEdge2Amount";
    private final long sumEdge1Amount;
    private final long sumEdge2Amount;

    public ComplexRead6Result(@JsonProperty(SUM_EDGE1_AMOUNT) long sumEdge1Amount,
                              @JsonProperty(SUM_EDGE2_AMOUNT) long sumEdge2Amount) {
        this.sumEdge1Amount = sumEdge1Amount;
        this.sumEdge2Amount = sumEdge2Amount;
    }

    public long getSumEdge1Amount() {
        return sumEdge1Amount;
    }

    public long getSumEdge2Amount() {
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
        return sumEdge1Amount == that.sumEdge1Amount
            && sumEdge2Amount == that.sumEdge2Amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sumEdge1Amount, sumEdge2Amount);
    }

    @Override
    public String toString() {
        return "ComplexRead6Result{"
            + "sumEdge1Amount="
            + sumEdge1Amount
            + ", sumEdge2Amount="
            + sumEdge2Amount
            + '}';
    }
}

