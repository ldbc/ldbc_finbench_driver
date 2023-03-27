package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead9Result {
    public static final String RATIO_REPAY = "ratioRepay";
    public static final String RATIO_OUT = "ratioOut";
    public static final String RATIO_IN = "ratioIn";
    private final float ratioRepay;
    private final float ratioOut;
    private final float ratioIn;

    public ComplexRead9Result(@JsonProperty(RATIO_REPAY) float ratioRepay,
                              @JsonProperty(RATIO_OUT) float ratioOut,
                              @JsonProperty(RATIO_IN) float ratioIn) {
        this.ratioRepay = ratioRepay;
        this.ratioOut = ratioOut;
        this.ratioIn = ratioIn;
    }

    public float getRatioRepay() {
        return ratioRepay;
    }

    public float getRatioOut() {
        return ratioOut;
    }

    public float getRatioIn() {
        return ratioIn;
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
        return ratioRepay == that.ratioRepay
            && ratioOut == that.ratioOut
            && ratioIn == that.ratioIn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ratioRepay, ratioOut, ratioIn);
    }

    @Override
    public String toString() {
        return "ComplexRead9Result{"
            + "ratioRepay="
            + ratioRepay
            + ", ratioOut="
            + ratioOut
            + ", ratioIn="
            + ratioIn
            + '}';
    }
}

