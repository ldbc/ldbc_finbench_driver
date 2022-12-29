package org.ldbcouncil.finbench.driver.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class ResultsLogValidationSummary {
    @JsonProperty(value = "excessive_delay_threshold_as_milli")
    private Long excessiveDelayThresholdAsMilli;

    @JsonProperty(value = "excessive_delay_count")
    private Long excessiveDelayCount;

    @JsonProperty(value = "excessive_delay_count_per_type")
    private Map<String, Long> excessiveDelayCountPerType;

    @JsonProperty(value = "min_delay_as_milli")
    private Long minDelayAsMilli;

    @JsonProperty(value = "max_delay_as_milli")
    private Long maxDelayAsMilli;

    @JsonProperty(value = "mean_delay_as_milli")
    private Long meanDelayAsMilli;

    @JsonProperty(value = "min_delay_as_milli_per_type")
    private Map<String, Long> minDelayAsMilliPerType;

    @JsonProperty(value = "max_delay_as_milli_per_type")
    private Map<String, Long> maxDelayAsMilliPerType;

    @JsonProperty(value = "mean_delay_as_milli_per_type")
    private Map<String, Long> meanDelayAsMilliPerType;

    public static ResultsLogValidationSummary fromJson(String jsonString) throws IOException {
        return new ObjectMapper().readValue(jsonString, ResultsLogValidationSummary.class);
    }

    private ResultsLogValidationSummary() {
    }

    ResultsLogValidationSummary(
        long excessiveDelayThresholdAsMilli,
        long excessiveDelayCount,
        Map<String, Long> excessiveDelayCountPerType,
        long minDelayAsMilli,
        long maxDelayAsMilli,
        long meanDelayAsMilli,
        Map<String, Long> minDelayAsMilliPerType,
        Map<String, Long> maxDelayAsMilliPerType,
        Map<String, Long> meanDelayAsMilliPerType) {
        this.excessiveDelayThresholdAsMilli = excessiveDelayThresholdAsMilli;
        this.excessiveDelayCount = excessiveDelayCount;
        this.excessiveDelayCountPerType = excessiveDelayCountPerType;
        this.minDelayAsMilli = minDelayAsMilli;
        this.maxDelayAsMilli = maxDelayAsMilli;
        this.meanDelayAsMilli = meanDelayAsMilli;
        this.minDelayAsMilliPerType = minDelayAsMilliPerType;
        this.maxDelayAsMilliPerType = maxDelayAsMilliPerType;
        this.meanDelayAsMilliPerType = meanDelayAsMilliPerType;
    }

    public Long excessiveDelayThresholdAsMilli() {
        return excessiveDelayThresholdAsMilli;
    }

    public Long excessiveDelayCount() {
        return excessiveDelayCount;
    }

    public Map<String, Long> excessiveDelayCountPerType() {
        return excessiveDelayCountPerType;
    }

    public Long minDelayAsMilli() {
        return minDelayAsMilli;
    }

    public Long maxDelayAsMilli() {
        return maxDelayAsMilli;
    }

    public Long meanDelayAsMilli() {
        return meanDelayAsMilli;
    }

    public Map<String, Long> minDelayAsMilliPerType() {
        return minDelayAsMilliPerType;
    }

    public Map<String, Long> maxDelayAsMilliPerType() {
        return maxDelayAsMilliPerType;
    }

    public Map<String, Long> meanDelayAsMilliPerType() {
        return meanDelayAsMilliPerType;
    }

    public String toJson() {
        try {
            return new ObjectMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing to JSON string", e);
        }
    }


    @Override
    public String toString() {
        return toJson();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResultsLogValidationSummary that = (ResultsLogValidationSummary) o;

        if (!Objects.equals(excessiveDelayCount, that.excessiveDelayCount)) {
            return false;
        }
        if (!Objects.equals(excessiveDelayCountPerType, that.excessiveDelayCountPerType)) {
            return false;
        }
        if (!Objects.equals(minDelayAsMilli, that.minDelayAsMilli)) {
            return false;
        }
        if (!Objects.equals(maxDelayAsMilli, that.maxDelayAsMilli)) {
            return false;
        }
        if (!Objects.equals(meanDelayAsMilli, that.meanDelayAsMilli)) {
            return false;
        }
        if (!Objects.equals(minDelayAsMilliPerType, that.minDelayAsMilliPerType)) {
            return false;
        }
        if (!Objects.equals(maxDelayAsMilliPerType, that.maxDelayAsMilliPerType)) {
            return false;
        }
        return !(!Objects.equals(meanDelayAsMilliPerType, that.meanDelayAsMilliPerType));

    }

    @Override
    public int hashCode() {
        int result = excessiveDelayCount != null ? excessiveDelayCount.hashCode() : 0;
        result = 31 * result + (excessiveDelayCountPerType != null ? excessiveDelayCountPerType.hashCode() : 0);
        result = 31 * result + (minDelayAsMilli != null ? minDelayAsMilli.hashCode() : 0);
        result = 31 * result + (maxDelayAsMilli != null ? maxDelayAsMilli.hashCode() : 0);
        result = 31 * result + (meanDelayAsMilli != null ? meanDelayAsMilli.hashCode() : 0);
        result = 31 * result + (minDelayAsMilliPerType != null ? minDelayAsMilliPerType.hashCode() : 0);
        result = 31 * result + (maxDelayAsMilliPerType != null ? maxDelayAsMilliPerType.hashCode() : 0);
        result = 31 * result + (meanDelayAsMilliPerType != null ? meanDelayAsMilliPerType.hashCode() : 0);
        return result;
    }
}
