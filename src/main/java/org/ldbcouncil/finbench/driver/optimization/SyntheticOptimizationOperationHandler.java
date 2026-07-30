/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

import java.util.Collections;
import org.ldbcouncil.finbench.driver.DbConnectionState;
import org.ldbcouncil.finbench.driver.DbException;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.OperationHandler;
import org.ldbcouncil.finbench.driver.ResultReporter;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;

public class SyntheticOptimizationOperationHandler
    implements OperationHandler<Operation, DbConnectionState> {
    private final OptimizationSupport support;
    private final OptimizationSimulationConfig config;

    public SyntheticOptimizationOperationHandler(
        OptimizationSupport support,
        OptimizationSimulationConfig config) {
        this.support = support;
        this.config = config;
    }

    @Override
    public void executeOperation(
        Operation operation,
        DbConnectionState dbConnectionState,
        ResultReporter resultReporter) throws DbException {
        support.executeSyntheticLoad(config.toSyntheticLoadSpec());
        reportSyntheticResult(operation, resultReporter);
    }

    private static void reportSyntheticResult(
        Operation operation, ResultReporter resultReporter) throws DbException {
        String operationName = operation.getClass().getSimpleName();
        if (operationName.startsWith("Write") || operationName.startsWith("ReadWrite")) {
            resultReporter.report(0, LdbcNoResult.INSTANCE, operation);
        } else {
            resultReporter.report(0, Collections.emptyList(), operation);
        }
    }
}
