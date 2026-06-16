/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OptimizationScenarioResult {
    @JsonProperty("operation")
    private final String operation;
    @JsonProperty("duration_reduction")
    private final double durationReduction;
    @JsonProperty("measured_throughput")
    private final double measuredThroughput;
    @JsonProperty("measured_throughput_gain")
    private final double measuredThroughputGain;
    @JsonProperty("target_duration_millis")
    private final double targetDurationMillis;
    @JsonProperty("scenario_results_dir")
    private final String scenarioResultsDir;
    @JsonProperty("profile_average_cpu_cores")
    private final double profileAverageCpuCores;
    @JsonProperty("profile_average_memory_bytes")
    private final double profileAverageMemoryBytes;
    @JsonProperty("profile_peak_memory_bytes")
    private final long profilePeakMemoryBytes;

    public OptimizationScenarioResult(
        String operation,
        double durationReduction,
        double measuredThroughput,
        double measuredThroughputGain,
        double targetDurationMillis,
        String scenarioResultsDir,
        double profileAverageCpuCores,
        double profileAverageMemoryBytes,
        long profilePeakMemoryBytes) {
        this.operation = operation;
        this.durationReduction = durationReduction;
        this.measuredThroughput = measuredThroughput;
        this.measuredThroughputGain = measuredThroughputGain;
        this.targetDurationMillis = targetDurationMillis;
        this.scenarioResultsDir = scenarioResultsDir;
        this.profileAverageCpuCores = profileAverageCpuCores;
        this.profileAverageMemoryBytes = profileAverageMemoryBytes;
        this.profilePeakMemoryBytes = profilePeakMemoryBytes;
    }

    public String operation() {
        return operation;
    }

    public double durationReduction() {
        return durationReduction;
    }

    public double measuredThroughput() {
        return measuredThroughput;
    }

    public double measuredThroughputGain() {
        return measuredThroughputGain;
    }

    public double targetDurationMillis() {
        return targetDurationMillis;
    }

    public String scenarioResultsDir() {
        return scenarioResultsDir;
    }
}
