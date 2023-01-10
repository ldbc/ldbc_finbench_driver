package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead5Result {
    public static final String SUM_EDGE_AMOUNT = "sumEdgeAmount";
    public static final String COUNT_EDGE = "countEdge";
    public static final String COUNT_OTHER_ACCOUNT = "countOtherAccount";
    private final long sumEdgeAmount;
    private final int countEdge;
    private final int countOtherAccount;

    public SimpleRead5Result(@JsonProperty(SUM_EDGE_AMOUNT) long sumEdgeAmount,
                             @JsonProperty(COUNT_EDGE) int countEdge,
                             @JsonProperty(COUNT_OTHER_ACCOUNT) int countOtherAccount) {
        this.sumEdgeAmount = sumEdgeAmount;
        this.countEdge = countEdge;
        this.countOtherAccount = countOtherAccount;
    }

    public long getSumEdgeAmount() {
        return sumEdgeAmount;
    }

    public int getCountEdge() {
        return countEdge;
    }

    public int getCountOtherAccount() {
        return countOtherAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead5Result that = (SimpleRead5Result) o;
        return sumEdgeAmount == that.sumEdgeAmount
            && countEdge == that.countEdge
            && countOtherAccount == that.countOtherAccount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sumEdgeAmount, countEdge, countOtherAccount);
    }

    @Override
    public String toString() {
        return "SimpleRead5Result{"
            + "sumEdgeAmount="
            + sumEdgeAmount
            + ", countEdge="
            + countEdge
            + ", countOtherAccount="
            + countOtherAccount
            + '}';
    }
}

