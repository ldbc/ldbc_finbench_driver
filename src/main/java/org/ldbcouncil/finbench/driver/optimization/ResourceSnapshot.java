/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceSnapshot {
    @JsonProperty("timestamp_millis")
    private final long timestampMillis;
    @JsonProperty("cpu_cores")
    private final double cpuCores;
    @JsonProperty("memory_bytes")
    private final long memoryBytes;

    public ResourceSnapshot(long timestampMillis, double cpuCores, long memoryBytes) {
        this.timestampMillis = timestampMillis;
        this.cpuCores = cpuCores;
        this.memoryBytes = memoryBytes;
    }

    public long timestampMillis() {
        return timestampMillis;
    }

    public double cpuCores() {
        return cpuCores;
    }

    public long memoryBytes() {
        return memoryBytes;
    }
}
