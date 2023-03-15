package org.ldbcouncil.finbench.driver.runtime.executor;

import static java.lang.String.format;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.ldbcouncil.finbench.driver.WorkloadStreams;
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeWriter;

public class OperationStreamExecutorService {
    public static final long SHUTDOWN_WAIT_TIMEOUT_AS_MILLI = TimeUnit.SECONDS.toMillis(10);

    private final OperationStreamExecutorServiceThread operationStreamExecutorServiceThread;
    private final AtomicBoolean hasFinished = new AtomicBoolean(false);
    private final ConcurrentErrorReporter errorReporter;
    private final AtomicBoolean executing = new AtomicBoolean(false);
    private final AtomicBoolean shutdown = new AtomicBoolean(false);
    private final AtomicBoolean forceThreadToTerminate = new AtomicBoolean(false);

    public OperationStreamExecutorService(
        ConcurrentErrorReporter errorReporter,
        WorkloadStreams.WorkloadStreamDefinition streamDefinition,
        OperationExecutor operationExecutor,
        CompletionTimeWriter completionTimeWriter
    ) {
        this.errorReporter = errorReporter;
        if (streamDefinition.dependencyOperations().hasNext() || streamDefinition.nonDependencyOperations().hasNext()) {
            this.operationStreamExecutorServiceThread = new OperationStreamExecutorServiceThread(
                operationExecutor,
                errorReporter,
                streamDefinition,
                hasFinished,
                forceThreadToTerminate,
                completionTimeWriter);
        } else {
            this.operationStreamExecutorServiceThread = null;
            executing.set(true);
            hasFinished.set(true);
            shutdown.set(false);
        }
    }

    public synchronized AtomicBoolean execute() {
        if (executing.get()) {
            return hasFinished;
        }
        executing.set(true);
        operationStreamExecutorServiceThread.start();
        return hasFinished;
    }

    public synchronized void shutdown(long shutdownWait) throws OperationExecutorException {
        if (shutdown.get()) {
            throw new OperationExecutorException("Executor has already been shutdown");
        }
        if (null != operationStreamExecutorServiceThread) {
            doShutdown(shutdownWait);
        }
        shutdown.set(true);
    }

    private void doShutdown(long shutdownWait) {
        try {
            forceThreadToTerminate.set(true);
            operationStreamExecutorServiceThread.join(shutdownWait);
        } catch (Exception e) {
            String errMsg = format("Unexpected error encountered while shutting down thread\n%s",
                ConcurrentErrorReporter.stackTraceToString(e));
            errorReporter.reportError(this, errMsg);
        }
    }
}
