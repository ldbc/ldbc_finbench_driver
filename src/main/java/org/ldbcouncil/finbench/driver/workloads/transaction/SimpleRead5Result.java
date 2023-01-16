package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead5Result {
    public static final String SUM_EDGE_AMOUNT = "sumEdgeAmount";
    public static final String NUM_EDGE = "numEdge";
    public static final String NUM_OTHER_ACCOUNT = "numOtherAccount";
    private final long sumEdgeAmount;
    private final int numEdge;
    private final int numOtherAccount;

    public SimpleRead5Result(@JsonProperty(SUM_EDGE_AMOUNT) long sumEdgeAmount,
                             @JsonProperty(NUM_EDGE) int numEdge,
                             @JsonProperty(NUM_OTHER_ACCOUNT) int numOtherAccount) {
        this.sumEdgeAmount = sumEdgeAmount;
        this.numEdge = numEdge;
        this.numOtherAccount = numOtherAccount;
    }

    public long getSumEdgeAmount() {
        return sumEdgeAmount;
    }

    public int getNumEdge() {
        return numEdge;
    }

    public int getNumOtherAccount() {
        return numOtherAccount;
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
            && numEdge == that.numEdge
            && numOtherAccount == that.numOtherAccount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sumEdgeAmount, numEdge, numOtherAccount);
    }

    @Override
    public String toString() {
        return "SimpleRead5Result{"
            + "sumEdgeAmount="
            + sumEdgeAmount
            + ", numEdge="
            + numEdge
            + ", numOtherAccount="
            + numOtherAccount
            + '}';
    }
}

