package org.ldbcouncil.finbench.driver.runtime.executor;

import java.util.concurrent.atomic.AtomicBoolean;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.WorkloadStreams;
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeWriter;
import org.ldbcouncil.finbench.driver.runtime.scheduling.Spinner;

class OperationStreamExecutorServiceThread extends Thread {
    private static final long POLL_INTERVAL_WHILE_WAITING_FOR_LAST_HANDLER_TO_FINISH_AS_MILLI = 100;

    private final OperationExecutor operationExecutor;
    private final ConcurrentErrorReporter errorReporter;
    private final AtomicBoolean hasFinished;
    private final AtomicBoolean forcedTerminate;
    private final InitiatedTimeSubmittingOperationRetriever initiatedTimeSubmittingOperationRetriever;

    public OperationStreamExecutorServiceThread(OperationExecutor operationExecutor,
                                                ConcurrentErrorReporter errorReporter,
                                                WorkloadStreams.WorkloadStreamDefinition streamDefinition,
                                                AtomicBoolean hasFinished,
                                                AtomicBoolean forcedTerminate,
                                                CompletionTimeWriter completionTimeWriter
    ) {
        super(OperationStreamExecutorServiceThread.class.getSimpleName() + "-" + System.currentTimeMillis());
        this.operationExecutor = operationExecutor;
        this.errorReporter = errorReporter;
        this.hasFinished = hasFinished;
        this.forcedTerminate = forcedTerminate;
        this.initiatedTimeSubmittingOperationRetriever = new InitiatedTimeSubmittingOperationRetriever(
            streamDefinition,
            completionTimeWriter
        );
    }

    @Override
    public void run() {
        try {
            while (initiatedTimeSubmittingOperationRetriever.hasNextOperation() && !forcedTerminate.get()) {
                Operation operation = initiatedTimeSubmittingOperationRetriever.nextOperation();
                // --- BLOCKING CALL (when bounded queue is full) ---
                operationExecutor.execute(operation);
            }
        } catch (Throwable e) {
            errorReporter.reportError(this, ConcurrentErrorReporter.stackTraceToString(e));
        } finally {
            while (0 < operationExecutor.uncompletedOperationHandlerCount() && !forcedTerminate.get()) {
                Spinner.powerNap(POLL_INTERVAL_WHILE_WAITING_FOR_LAST_HANDLER_TO_FINISH_AS_MILLI);
            }
            this.hasFinished.set(true);
        }
    }
}
