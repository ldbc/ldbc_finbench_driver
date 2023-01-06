package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead13Result {
    public static final String SUM_EDGE2_AMOUNT = "sumEdge2Amount";
    private final long sumEdge2Amount;

    public ComplexRead13Result(@JsonProperty(SUM_EDGE2_AMOUNT) long sumEdge2Amount) {
        this.sumEdge2Amount = sumEdge2Amount;
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
        ComplexRead13Result that = (ComplexRead13Result) o;
        return sumEdge2Amount == that.sumEdge2Amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sumEdge2Amount);
    }

    @Override
    public String toString() {
        return "ComplexRead13Result{"
            + "sumEdge2Amount="
            + sumEdge2Amount
            + '}';
    }
}

