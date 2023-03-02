package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead9Result {
    public static final String RATIO1 = "ratio1";
    public static final String RATIO2 = "ratio2";
    public static final String RATIO3 = "ratio3";
    private final float ratio1;
    private final float ratio2;
    private final float ratio3;

    public ComplexRead9Result(@JsonProperty(RATIO1) float ratio1,
                              @JsonProperty(RATIO2) float ratio2,
                              @JsonProperty(RATIO3) float ratio3) {
        this.ratio1 = ratio1;
        this.ratio2 = ratio2;
        this.ratio3 = ratio3;
    }

    public float getRatio1() {
        return ratio1;
    }

    public float getRatio2() {
        return ratio2;
    }

    public float getRatio3() {
        return ratio3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead9Result that = (ComplexRead9Result) o;
        return ratio1 == that.ratio1
            && ratio2 == that.ratio2
            && ratio3 == that.ratio3;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ratio1, ratio2, ratio3);
    }

    @Override
    public String toString() {
        return "ComplexRead9Result{"
            + "ratio1="
            + ratio1
            + ", ratio2="
            + ratio2
            + ", ratio3="
            + ratio3
            + '}';
    }
}

