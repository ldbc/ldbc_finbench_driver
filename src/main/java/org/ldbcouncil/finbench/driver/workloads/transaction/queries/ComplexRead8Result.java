package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead8Result {
    public static final String DST_ID = "dstId";
    public static final String RATIO = "ratio";
    public static final String DISTANCE_FROM_LOAN = "distanceFromLoan";
    private final long dstId;
    private final float ratio;
    private final int distanceFromLoan;

    public ComplexRead8Result(@JsonProperty(DST_ID) long dstId,
                              @JsonProperty(RATIO) float ratio,
                              @JsonProperty(DISTANCE_FROM_LOAN) int distanceFromLoan) {
        this.dstId = dstId;
        this.ratio = ratio;
        this.distanceFromLoan = distanceFromLoan;
    }

    public long getDstId() {
        return dstId;
    }

    public float getRatio() {
        return ratio;
    }

    public int getDistanceFromLoan() {
        return distanceFromLoan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead8Result that = (ComplexRead8Result) o;
        return dstId == that.dstId
            && ratio == that.ratio
            && distanceFromLoan == that.distanceFromLoan;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dstId, ratio, distanceFromLoan);
    }

    @Override
    public String toString() {
        return "ComplexRead8Result{"
            + "dstId="
            + dstId
            + ", ratio="
            + ratio
            + ", distanceFromLoan="
            + distanceFromLoan
            + '}';
    }
}

