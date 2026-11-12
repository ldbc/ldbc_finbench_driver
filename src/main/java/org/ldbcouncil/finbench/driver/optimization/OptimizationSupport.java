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
    /**
     * Returns the current CPU usage of the system under test in cores.
     */
    double getCurrentCpuCores() throws DbException;

    /**
     * Returns the current memory usage of the system under test in bytes.
     */
    long getCurrentMemoryBytes() throws DbException;

    /**
     * Executes a synthetic operation using the CPU, memory and duration requested by the driver.
     */
    SyntheticExecutionResult executeSyntheticLoad(SyntheticLoadSpec spec) throws DbException;
}
