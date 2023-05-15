package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead5Result {
    public static final String SRC_ID = "srcId";
    public static final String NUM_EDGES = "numEdges";
    public static final String SUM_AMOUNT = "sumAmount";
    private final long srcId;
    private final int numEdges;
    private final double sumAmount;

    public SimpleRead5Result(@JsonProperty(SRC_ID) long srcId,
                             @JsonProperty(NUM_EDGES) int numEdges,
                             @JsonProperty(SUM_AMOUNT) double sumAmount) {
        this.srcId = srcId;
        this.numEdges = numEdges;
        this.sumAmount = sumAmount;
    }

    public long getSrcId() {
        return srcId;
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
        SimpleRead5Result that = (SimpleRead5Result) o;
        return srcId == that.srcId
            && numEdges == that.numEdges
            && sumAmount == that.sumAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcId, numEdges, sumAmount);
    }

    @Override
    public String toString() {
        return "SimpleRead5Result{"
            + "srcId="
            + srcId
            + ", numEdges="
            + numEdges
            + ", sumAmount="
            + sumAmount
            + '}';
    }
}

