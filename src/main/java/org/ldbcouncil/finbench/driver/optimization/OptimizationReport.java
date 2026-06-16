/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class OptimizationReport {
    @JsonProperty("baseline_throughput")
    private final double baselineThroughput;
    @JsonProperty("ranked_candidates")
    private final List<OptimizationCandidate> rankedCandidates;
    @JsonProperty("resource_profiles")
    private final List<OperationResourceProfile> resourceProfiles;
    @JsonProperty("scenarios")
    private final List<OptimizationScenarioResult> scenarios;
    @JsonProperty("simulation_supported")
    private final boolean simulationSupported;
    @JsonProperty("notice")
    private final String notice;

    public OptimizationReport(
        double baselineThroughput,
        List<OptimizationCandidate> rankedCandidates,
        List<OperationResourceProfile> resourceProfiles,
        List<OptimizationScenarioResult> scenarios,
        boolean simulationSupported) {
        this.baselineThroughput = baselineThroughput;
        this.rankedCandidates = rankedCandidates;
        this.resourceProfiles = resourceProfiles;
        this.scenarios = scenarios;
        this.simulationSupported = simulationSupported;
        this.notice = "Synthetic scenario results are measured by rerunning the full workload with only the target "
            + "operation simulated. Verify recommendations again after implementing the real SUT optimization.";
    }

    public String toJson() {
        try {
            return new ObjectMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("Unable to serialize optimization report", e);
        }
    }

    public String toMarkdown() {
        StringBuilder builder = new StringBuilder();
        builder.append("# FinBench Optimization Recommendation\n\n");
        builder.append("Baseline throughput: ").append(baselineThroughput).append(" ops/s\n\n");
        builder.append("## Ranked Candidates\n\n");
        builder.append("| Rank | Operation | Count | Mean ms | Service time share |\n");
        builder.append("|---:|---|---:|---:|---:|\n");
        int rank = 1;
        for (OptimizationCandidate candidate : rankedCandidates) {
            builder.append("| ").append(rank++).append(" | ").append(candidate.operation()).append(" | ")
                .append(candidate.count()).append(" | ").append(candidate.meanDurationMillis()).append(" | ")
                .append(candidate.serviceTimeShare() * 100).append("% |\n");
        }
        builder.append("\n## Resource Profiles\n\n");
        builder.append("| Operation | Profile duration ms | Avg CPU cores | Avg memory bytes | Peak memory bytes |\n");
        builder.append("|---|---:|---:|---:|---:|\n");
        for (OperationResourceProfile profile : resourceProfiles) {
            builder.append("| ").append(profile.operation()).append(" | ")
                .append(profile.averageDurationMillis()).append(" | ")
                .append(profile.averageCpuCores()).append(" | ")
                .append(profile.averageMemoryBytes()).append(" | ")
                .append(profile.peakMemoryBytes()).append(" |\n");
        }
        builder.append("\n## Synthetic Scenarios\n\n");
        builder.append("| Operation | Duration reduction | Measured throughput | Measured gain |")
            .append(" Target duration ms | Scenario results |\n");
        builder.append("|---|---:|---:|---:|---:|---|\n");
        for (OptimizationScenarioResult scenario : scenarios) {
            builder.append("| ").append(scenario.operation()).append(" | ")
                .append(scenario.durationReduction() * 100).append("% | ")
                .append(scenario.measuredThroughput()).append(" | ")
                .append(scenario.measuredThroughputGain() * 100).append("% | ")
                .append(scenario.targetDurationMillis()).append(" | ")
                .append(scenario.scenarioResultsDir()).append(" |\n");
        }
        builder.append("\n## Notice\n\n").append(notice).append("\n");
        return builder.toString();
    }
}
