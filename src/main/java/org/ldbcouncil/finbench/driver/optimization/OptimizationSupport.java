/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

import org.ldbcouncil.finbench.driver.DbException;

/**
 * Optional capability implemented by a system under test for optimization experiments.
 */
public interface OptimizationSupport {
    ResourceSnapshot sampleResources() throws DbException;

    default SyntheticExecutionResult executeRepresentativeOperation(OperationProfileSpec spec) throws DbException {
        return executeSyntheticLoad(new SyntheticLoadSpec(
            spec.operationName(),
            0,
            0,
            spec.expectedDurationMillis()
        ));
    }

    SyntheticExecutionResult executeSyntheticLoad(SyntheticLoadSpec spec) throws DbException;
}
