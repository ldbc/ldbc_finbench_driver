/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SyntheticExecutionResult {
    @JsonProperty("actual_duration_millis")
    private final long actualDurationMillis;
    @JsonProperty("peak_memory_bytes")
    private final long peakMemoryBytes;

    public SyntheticExecutionResult(long actualDurationMillis, long peakMemoryBytes) {
        this.actualDurationMillis = actualDurationMillis;
        this.peakMemoryBytes = peakMemoryBytes;
    }

    public long actualDurationMillis() {
        return actualDurationMillis;
    }

    public long peakMemoryBytes() {
        return peakMemoryBytes;
    }
}
