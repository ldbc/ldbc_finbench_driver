package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead11Result {
    public static final String FINAL_SHARE = "finalShare";
    private final float finalShare;

    public ComplexRead11Result(@JsonProperty(FINAL_SHARE) float finalShare) {
        this.finalShare = finalShare;
    }

    public float getFinalShare() {
        return finalShare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead11Result that = (ComplexRead11Result) o;
        return finalShare == that.finalShare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(finalShare);
    }

    @Override
    public String toString() {
        return "ComplexRead11Result{"
            + "finalShare="
            + finalShare
            + '}';
    }
}

