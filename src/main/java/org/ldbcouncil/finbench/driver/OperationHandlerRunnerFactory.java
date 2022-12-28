package org.ldbcouncil.finbench.driver;

public interface OperationHandlerRunnerFactory {
    OperationHandlerRunnableContext newOperationHandlerRunner() throws OperationException;

    void shutdown() throws OperationException;
}
