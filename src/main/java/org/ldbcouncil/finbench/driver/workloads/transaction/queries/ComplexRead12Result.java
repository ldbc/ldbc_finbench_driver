package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead12Result {
    public static final String COMP_ACCOUNT_ID = "compAccountId";
    public static final String SUM_EDGE2_AMOUNT = "sumEdge2Amount";
    private final long compAccountId;
    private final double sumEdge2Amount;

    public ComplexRead12Result(@JsonProperty(COMP_ACCOUNT_ID) long compAccountId,
                               @JsonProperty(SUM_EDGE2_AMOUNT) double sumEdge2Amount) {
        this.compAccountId = compAccountId;
        this.sumEdge2Amount = sumEdge2Amount;
    }

    public long getCompAccountId() {
        return compAccountId;
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
        ComplexRead12Result that = (ComplexRead12Result) o;
        return compAccountId == that.compAccountId
            && sumEdge2Amount == that.sumEdge2Amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(compAccountId, sumEdge2Amount);
    }

    @Override
    public String toString() {
        return "ComplexRead12Result{"
            + "compAccountId="
            + compAccountId
            + ", sumEdge2Amount="
            + sumEdge2Amount
            + '}';
    }
}

