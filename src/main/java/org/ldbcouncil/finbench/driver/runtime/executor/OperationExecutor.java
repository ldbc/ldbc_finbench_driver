package org.ldbcouncil.finbench.driver.runtime.executor;


import org.ldbcouncil.finbench.driver.Operation;

public interface OperationExecutor {
    /**
     * @param operation
     * @return
     */
    void execute(Operation operation) throws OperationExecutorException;

    /**
     * Returns after executor has completed shutting down
     *
     * @param waitAsMilli duration to wait for all running operation handlers to complete execution
     * @throws OperationExecutorException
     */
    void shutdown(long waitAsMilli) throws OperationExecutorException;

    long uncompletedOperationHandlerCount();
}
