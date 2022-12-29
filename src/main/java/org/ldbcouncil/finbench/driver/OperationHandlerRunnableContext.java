package org.ldbcouncil.finbench.driver;

import static java.lang.String.format;

import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeWriter;
import org.ldbcouncil.finbench.driver.runtime.metrics.MetricsCollectionException;
import org.ldbcouncil.finbench.driver.runtime.metrics.MetricsService;
import org.ldbcouncil.finbench.driver.runtime.scheduling.Spinner;
import org.ldbcouncil.finbench.driver.runtime.scheduling.SpinnerCheck;
import org.ldbcouncil.finbench.driver.temporal.TimeSource;
import stormpot.Poolable;
import stormpot.Slot;

public class OperationHandlerRunnableContext implements Runnable, Poolable {
    // set by OperationHandlerRunnerFactory
    private Slot slot = null;

    // set by Db
    private DbConnectionState dbConnectionState = null;
    private OperationHandler operationHandler = null;

    // set by DependencyAndNonDependencyHandlersRetriever
    private TimeSource timeSource = null;
    private Spinner spinner = null;
    private Operation operation = null;
    private CompletionTimeWriter completionTimeWriter = null;
    private ConcurrentErrorReporter errorReporter = null;
    private MetricsService.MetricsServiceWriter metricsServiceWriter = null;

    // set by DependencyAndNonDependencyHandlersRetriever
    private SpinnerCheck beforeExecuteCheck = null;

    private boolean initialized = false;

    private ResultReporter.SimpleResultReporter resultReporter = null;

    final void setSlot(Slot slot) {
        this.slot = slot;
    }

    public final void init(TimeSource timeSource,
                           Spinner spinner,
                           Operation operation,
                           CompletionTimeWriter completionTimeWriter,
                           ConcurrentErrorReporter errorReporter,
                           MetricsService metricsService) throws OperationException {
        if (initialized) {
            throw new OperationException(format("%s can not be initialized twice", getClass().getSimpleName()));
        }
        if (null == this.timeSource) {
            this.timeSource = timeSource;
            this.spinner = spinner;
            this.errorReporter = errorReporter;
            if (null == resultReporter) {
                this.resultReporter = new ResultReporter.SimpleResultReporter(errorReporter);
            }
            try {
                this.metricsServiceWriter = metricsService.getWriter();
            } catch (MetricsCollectionException e) {
                throw new OperationException("Error while retrieving metrics writer", e);
            }
        }
        this.operation = operation;
        this.completionTimeWriter = completionTimeWriter;
        this.beforeExecuteCheck = Spinner.TRUE_CHECK;
        this.initialized = true;
    }

    final void setOperationHandler(OperationHandler operationHandler) {
        this.operationHandler = operationHandler;
    }

    final void setDbConnectionState(DbConnectionState dbConnectionState) {
        this.dbConnectionState = dbConnectionState;
    }

    public final void setBeforeExecuteCheck(SpinnerCheck check) {
        beforeExecuteCheck = check;
    }

    public final Operation operation() {
        return operation;
    }

    public final OperationHandler operationHandler() {
        return operationHandler;
    }

    public final DbConnectionState dbConnectionState() {
        return dbConnectionState;
    }

    public final ResultReporter resultReporter() {
        return resultReporter;
    }

    /**
     * Internally calls the method executeOperation(operation)
     * and returns the associated OperationResultReport if execution was successful.
     * If execution is successful OperationResultReport metrics are also written to ConcurrentMetricsService.
     * If execution is unsuccessful the result is null, an error is written to ConcurrentErrorReporter,
     * and no metrics are written.
     */
    @Override
    public void run() {
        if (!initialized) {
            errorReporter.reportError(this, "Handler was executed before being initialized");
            return;
        }
        try {
            if (!spinner.waitForScheduledStartTime(operation, beforeExecuteCheck)) {
                // TODO something more elaborate here? see comments in Spinner
                // TODO should probably report failed operation
                // Spinner result indicates operation should not be processed
                return;
            }
            resultReporter.setActualStartTimeAsMilli(timeSource.nowAsMilli());
            long startOfLatencyMeasurementAsNano = timeSource.nanoSnapshot();
            operationHandler.executeOperation(operation, dbConnectionState, resultReporter);
            long endOfLatencyMeasurementAsNano = timeSource.nanoSnapshot();
            resultReporter.setRunDurationAsNano(endOfLatencyMeasurementAsNano - startOfLatencyMeasurementAsNano);
            if (null == resultReporter().result()) {
                errorReporter.reportError(this, format("Operation result is null\nOperation: %s", operation));
            } else {
                completionTimeWriter.submitCompletedTime(operation.timeStamp());
                metricsServiceWriter.submitOperationResult(
                    operation.type(),
                    operation.scheduledStartTimeAsMilli(),
                    resultReporter.actualStartTimeAsMilli(),
                    resultReporter.runDurationAsNano(),
                    resultReporter.resultCode(),
                    operation.timeStamp()
                );
            }
        } catch (Throwable e) {
            String errMsg = format("Error encountered\n%s\n%s",
                operation,
                ConcurrentErrorReporter.stackTraceToString(e));
            errorReporter.reportError(this, errMsg);
        }
    }


    @Override
    public String toString() {
        return "OperationHandlerRunner\n" + "    -> resultReporter=" + resultReporter + "\n" + "    -> slot=" + slot
            + "\n" + "    -> operation=" + operation + "\n" + "    -> beforeExecuteCheck=" + beforeExecuteCheck + "\n"
            + "    -> operationHandler=" + operationHandler + "\n" + "    -> initialized=" + initialized;
    }

    public final void cleanup() {
        release();
    }

    // Note, this should not really be public API, it is from the StormPot Poolable interface
    @Override
    public final void release() {
        initialized = false;
        if (null != slot) {
            slot.release(this);
        }
    }
}
