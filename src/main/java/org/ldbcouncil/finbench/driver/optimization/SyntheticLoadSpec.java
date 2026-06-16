/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

public class SyntheticLoadSpec {
    private final String operationName;
    private final double targetCpuCores;
    private final long targetMemoryBytes;
    private final long targetDurationMillis;

    public SyntheticLoadSpec(
        String operationName,
        double targetCpuCores,
        long targetMemoryBytes,
        long targetDurationMillis) {
        this.operationName = operationName;
        this.targetCpuCores = targetCpuCores;
        this.targetMemoryBytes = targetMemoryBytes;
        this.targetDurationMillis = targetDurationMillis;
    }

    public String operationName() {
        return operationName;
    }

    public double targetCpuCores() {
        return targetCpuCores;
    }

    public long targetMemoryBytes() {
        return targetMemoryBytes;
    }

    public long targetDurationMillis() {
        return targetDurationMillis;
    }
}
