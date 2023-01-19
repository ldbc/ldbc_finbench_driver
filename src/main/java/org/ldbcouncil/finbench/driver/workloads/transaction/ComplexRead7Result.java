package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead7Result {
    public static final String COUNT_SRC = "countSrc";
    public static final String NUM_DST = "numDst";
    public static final String RATIO = "ratio";
    private final int countSrc;
    private final int numDst;
    private final float ratio;

    public ComplexRead7Result(@JsonProperty(COUNT_SRC) int countSrc,
                              @JsonProperty(NUM_DST) int numDst,
                              @JsonProperty(RATIO) float ratio) {
        this.countSrc = countSrc;
        this.numDst = numDst;
        this.ratio = ratio;
    }

    public int getCountSrc() {
        return countSrc;
    }

    public int getNumDst() {
        return numDst;
    }

    public float getRatio() {
        return ratio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead7Result that = (ComplexRead7Result) o;
        return countSrc == that.countSrc
            && numDst == that.numDst
            && ratio == that.ratio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(countSrc, numDst, ratio);
    }

    @Override
    public String toString() {
        return "ComplexRead7Result{"
            + "countSrc="
            + countSrc
            + ", numDst="
            + numDst
            + ", ratio="
            + ratio
            + '}';
    }
}

