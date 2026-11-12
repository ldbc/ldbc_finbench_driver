/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationResourceProfile {
    @JsonProperty("operation")
    private final String operation;
    @JsonProperty("average_duration_millis")
    private final double averageDurationMillis;
    @JsonProperty("average_cpu_cores")
    private final double averageCpuCores;
    @JsonProperty("average_memory_bytes")
    private final double averageMemoryBytes;
    @JsonProperty("peak_memory_bytes")
    private final long peakMemoryBytes;
    @JsonProperty("resource_sample_count")
    private final long resourceSampleCount;

    public OperationResourceProfile(
        String operation,
        double averageDurationMillis,
        double averageCpuCores,
        double averageMemoryBytes,
        long peakMemoryBytes,
        long resourceSampleCount) {
        this.operation = operation;
        this.averageDurationMillis = averageDurationMillis;
        this.averageCpuCores = averageCpuCores;
        this.averageMemoryBytes = averageMemoryBytes;
        this.peakMemoryBytes = peakMemoryBytes;
        this.resourceSampleCount = resourceSampleCount;
    }

    public String operation() {
        return operation;
    }

    public double averageDurationMillis() {
        return averageDurationMillis;
    }

    public double averageCpuCores() {
        return averageCpuCores;
    }

    public double averageMemoryBytes() {
        return averageMemoryBytes;
    }

    public long peakMemoryBytes() {
        return peakMemoryBytes;
    }
}
