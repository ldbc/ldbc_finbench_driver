package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead4Result {
    public static final String DST_ID = "dstId";
    public static final String NUM_EDGES = "numEdges";
    public static final String SUM_AMOUNT = "sumAmount";
    private final long dstId;
    private final int numEdges;
    private final double sumAmount;

    public SimpleRead4Result(@JsonProperty(DST_ID) long dstId,
                             @JsonProperty(NUM_EDGES) int numEdges,
                             @JsonProperty(SUM_AMOUNT) double sumAmount) {
        this.dstId = dstId;
        this.numEdges = numEdges;
        this.sumAmount = sumAmount;
    }

    public long getDstId() {
        return dstId;
    }

    public int getNumEdges() {
        return numEdges;
    }

    public double getSumAmount() {
        return sumAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead4Result that = (SimpleRead4Result) o;
        return dstId == that.dstId
            && numEdges == that.numEdges
            && sumAmount == that.sumAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dstId, numEdges, sumAmount);
    }

    @Override
    public String toString() {
        return "SimpleRead4Result{"
            + "dstId="
            + dstId
            + ", numEdges="
            + numEdges
            + ", sumAmount="
            + sumAmount
            + '}';
    }
}

