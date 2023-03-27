package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead3Result {
    public static final String BLOCK_RATIO = "blockRatio";
    private final float blockRatio;

    public SimpleRead3Result(@JsonProperty(BLOCK_RATIO) float blockRatio) {
        this.blockRatio = blockRatio;
    }

    public float getBlockRatio() {
        return blockRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead3Result that = (SimpleRead3Result) o;
        return blockRatio == that.blockRatio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockRatio);
    }

    @Override
    public String toString() {
        return "SimpleRead3Result{"
            + "blockRatio="
            + blockRatio
            + '}';
    }
}

