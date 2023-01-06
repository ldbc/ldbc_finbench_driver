package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead10Result {
    public static final String JACCARD = "jaccard";
    private final float jaccard;

    public ComplexRead10Result(@JsonProperty(JACCARD) float jaccard) {
        this.jaccard = jaccard;
    }

    public float getJaccard() {
        return jaccard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead10Result that = (ComplexRead10Result) o;
        return jaccard == that.jaccard;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jaccard);
    }

    @Override
    public String toString() {
        return "ComplexRead10Result{"
            + "jaccard="
            + jaccard
            + '}';
    }
}

