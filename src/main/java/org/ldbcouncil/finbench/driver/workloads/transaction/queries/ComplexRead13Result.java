package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead13Result {
    public static final String COMP_ACCOUNT_ID = "compAccountId";
    public static final String SUM_EDGE2_AMOUNT = "sumEdge2Amount";
    private final long compAccountId;
    private final int sumEdge2Amount;

    public ComplexRead13Result(@JsonProperty(COMP_ACCOUNT_ID) long compAccountId,
                               @JsonProperty(SUM_EDGE2_AMOUNT) int sumEdge2Amount) {
        this.compAccountId = compAccountId;
        this.sumEdge2Amount = sumEdge2Amount;
    }

    public long getCompAccountId() {
        return compAccountId;
    }

    public int getSumEdge2Amount() {
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
        return compAccountId == that.compAccountId
            && sumEdge2Amount == that.sumEdge2Amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(compAccountId, sumEdge2Amount);
    }

    @Override
    public String toString() {
        return "ComplexRead13Result{"
            + "compAccountId="
            + compAccountId
            + ", sumEdge2Amount="
            + sumEdge2Amount
            + '}';
    }
}

