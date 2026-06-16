/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OptimizationCandidate {
    @JsonProperty("operation")
    private final String operation;
    @JsonProperty("count")
    private final long count;
    @JsonProperty("mean_duration_millis")
    private final double meanDurationMillis;
    @JsonProperty("p95_duration_millis")
    private final double p95DurationMillis;
    @JsonProperty("total_service_time_millis")
    private final double totalServiceTimeMillis;
    @JsonProperty("service_time_share")
    private final double serviceTimeShare;
    @JsonProperty("recommendation_score")
    private final double recommendationScore;

    public OptimizationCandidate(
        String operation,
        long count,
        double meanDurationMillis,
        double p95DurationMillis,
        double totalServiceTimeMillis,
        double serviceTimeShare,
        double recommendationScore) {
        this.operation = operation;
        this.count = count;
        this.meanDurationMillis = meanDurationMillis;
        this.p95DurationMillis = p95DurationMillis;
        this.totalServiceTimeMillis = totalServiceTimeMillis;
        this.serviceTimeShare = serviceTimeShare;
        this.recommendationScore = recommendationScore;
    }

    public String operation() {
        return operation;
    }

    public long count() {
        return count;
    }

    public double meanDurationMillis() {
        return meanDurationMillis;
    }

    public double serviceTimeShare() {
        return serviceTimeShare;
    }

    public double recommendationScore() {
        return recommendationScore;
    }
}
