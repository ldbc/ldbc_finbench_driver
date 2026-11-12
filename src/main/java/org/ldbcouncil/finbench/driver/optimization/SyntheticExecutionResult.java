/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SyntheticExecutionResult {
    @JsonProperty("actual_duration_nanos")
    private final long actualDurationNanos;
    @JsonProperty("peak_memory_bytes")
    private final long peakMemoryBytes;

    public SyntheticExecutionResult(long actualDurationNanos, long peakMemoryBytes) {
        this.actualDurationNanos = actualDurationNanos;
        this.peakMemoryBytes = peakMemoryBytes;
    }

    public long actualDurationNanos() {
        return actualDurationNanos;
    }

    public long peakMemoryBytes() {
        return peakMemoryBytes;
    }
}
