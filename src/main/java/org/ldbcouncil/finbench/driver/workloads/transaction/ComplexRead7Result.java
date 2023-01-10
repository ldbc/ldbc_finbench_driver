package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead7Result {
    public static final String COUNT_SRC = "countSrc";
    public static final String COUNT_DST = "countDst";
    public static final String RATIO = "ratio";
    private final int countSrc;
    private final int countDst;
    private final float ratio;

    public ComplexRead7Result(@JsonProperty(COUNT_SRC) int countSrc,
                              @JsonProperty(COUNT_DST) int countDst,
                              @JsonProperty(RATIO) float ratio) {
        this.countSrc = countSrc;
        this.countDst = countDst;
        this.ratio = ratio;
    }

    public int getCountSrc() {
        return countSrc;
    }

    public int getCountDst() {
        return countDst;
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
            && countDst == that.countDst
            && ratio == that.ratio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(countSrc, countDst, ratio);
    }

    @Override
    public String toString() {
        return "ComplexRead7Result{"
            + "countSrc="
            + countSrc
            + ", countDst="
            + countDst
            + ", ratio="
            + ratio
            + '}';
    }
}

