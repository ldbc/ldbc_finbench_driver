package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead10Result {
    public static final String JACCARD_SIMILARITY = "jaccardSimilarity";
    private final float jaccardSimilarity;

    public ComplexRead10Result(@JsonProperty(JACCARD_SIMILARITY) float jaccardSimilarity) {
        this.jaccardSimilarity = jaccardSimilarity;
    }

    public float getJaccardSimilarity() {
        return jaccardSimilarity;
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
        return jaccardSimilarity == that.jaccardSimilarity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jaccardSimilarity);
    }

    @Override
    public String toString() {
        return "ComplexRead10Result{"
            + "jaccardSimilarity="
            + jaccardSimilarity
            + '}';
    }
}

