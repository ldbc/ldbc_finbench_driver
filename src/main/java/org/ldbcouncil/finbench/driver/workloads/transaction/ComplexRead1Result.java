package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead1Result {
    public static final String COUNT_MEDIUM = "countMedium";
    public static final String MEDIUM_TYPE = "mediumType";
    public static final String MEDIUM_ID = "mediumId";
    private final long countMedium;
    private final String mediumType;
    private final String mediumId;

    public ComplexRead1Result(@JsonProperty(COUNT_MEDIUM) long countMedium,
                              @JsonProperty(MEDIUM_TYPE) String mediumType,
                              @JsonProperty(MEDIUM_ID) String mediumId) {
        this.countMedium = countMedium;
        this.mediumType = mediumType;
        this.mediumId = mediumId;
    }

    public long getCountMedium() {
        return countMedium;
    }

    public String getMediumType() {
        return mediumType;
    }

    public String getMediumId() {
        return mediumId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead1Result that = (ComplexRead1Result) o;
        return countMedium == that.countMedium
            && Objects.equals(mediumType, that.mediumType)
            && Objects.equals(mediumId, that.mediumId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countMedium, mediumType, mediumId);
    }

    @Override
    public String toString() {
        return "ComplexRead1Result{"
            + "countMedium="
            + countMedium
            + ", mediumType="
            + mediumType
            + ", mediumId="
            + mediumId
            + '}';
    }
}

