/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

public class OperationProfileSpec {
    private final String operationName;
    private final long expectedDurationMillis;

    public OperationProfileSpec(String operationName, long expectedDurationMillis) {
        this.operationName = operationName;
        this.expectedDurationMillis = expectedDurationMillis;
    }

    public String operationName() {
        return operationName;
    }

    public long expectedDurationMillis() {
        return expectedDurationMillis;
    }
}
