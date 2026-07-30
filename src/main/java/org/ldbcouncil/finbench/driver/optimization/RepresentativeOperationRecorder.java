/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.ldbcouncil.finbench.driver.Operation;

/**
 * Retains one real operation, including its parameters, for each operation type seen in a workload.
 */
public class RepresentativeOperationRecorder {
    private final ConcurrentMap<String, Operation> operations = new ConcurrentHashMap<>();

    public void record(Operation operation) {
        String operationName = operation.getClass().getSimpleName();
        if (!operations.containsKey(operationName)) {
            operations.putIfAbsent(operationName, operation.newInstance());
        }
    }

    public Optional<Operation> operation(String operationName) {
        Operation operation = operations.get(operationName);
        return operation == null ? Optional.empty() : Optional.of(operation.newInstance());
    }

    public Map<Integer, Class<? extends Operation>> operationTypeToClassMapping() {
        Map<Integer, Class<? extends Operation>> mapping = new HashMap<>();
        for (Operation operation : operations.values()) {
            mapping.put(operation.type(), operation.getClass());
        }
        return mapping;
    }
}
