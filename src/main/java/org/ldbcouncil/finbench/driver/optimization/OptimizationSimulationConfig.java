/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

import java.util.Map;
import java.util.Optional;

public class OptimizationSimulationConfig {
    public static final String ENABLED = "optimization.simulation.enabled";
    public static final String OPERATION = "optimization.simulation.operation";
    public static final String TARGET_CPU_CORES = "optimization.simulation.target_cpu_cores";
    public static final String TARGET_MEMORY_BYTES = "optimization.simulation.target_memory_bytes";
    public static final String TARGET_DURATION_NANOS =
        "optimization.simulation.target_duration_" + "nanos";

    private final String operationName;
    private final double targetCpuCores;
    private final long targetMemoryBytes;
    private final long targetDurationNanos;

    public static Optional<OptimizationSimulationConfig> from(Map<String, String> params) {
        if (!Boolean.parseBoolean(params.get(ENABLED)) || !params.containsKey(OPERATION)) {
            return Optional.empty();
        }
        return Optional.of(new OptimizationSimulationConfig(
            params.get(OPERATION),
            doubleParam(params, TARGET_CPU_CORES, 0),
            longParam(params, TARGET_MEMORY_BYTES, 0),
            longParam(params, TARGET_DURATION_NANOS, 1)
        ));
    }

    private OptimizationSimulationConfig(
        String operationName,
        double targetCpuCores,
        long targetMemoryBytes,
        long targetDurationNanos) {
        this.operationName = operationName;
        this.targetCpuCores = targetCpuCores;
        this.targetMemoryBytes = targetMemoryBytes;
        this.targetDurationNanos = targetDurationNanos;
    }

    public boolean matches(String operationName) {
        return this.operationName.equals(operationName);
    }

    public SyntheticLoadSpec toSyntheticLoadSpec() {
        return new SyntheticLoadSpec(
            operationName, targetCpuCores, targetMemoryBytes, targetDurationNanos);
    }

    private static long longParam(Map<String, String> params, String key, long defaultValue) {
        return params.containsKey(key) ? Long.parseLong(params.get(key)) : defaultValue;
    }

    private static double doubleParam(Map<String, String> params, String key, double defaultValue) {
        return params.containsKey(key) ? Double.parseDouble(params.get(key)) : defaultValue;
    }
}
