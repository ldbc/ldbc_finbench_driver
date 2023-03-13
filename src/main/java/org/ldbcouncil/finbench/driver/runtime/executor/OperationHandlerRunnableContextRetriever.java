package org.ldbcouncil.finbench.driver.runtime.executor;


import static java.lang.String.format;

import java.util.Set;
import org.ldbcouncil.finbench.driver.Db;
import org.ldbcouncil.finbench.driver.DbException;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.OperationHandlerRunnableContext;
import org.ldbcouncil.finbench.driver.WorkloadStreams;
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeException;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeReader;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeWriter;
import org.ldbcouncil.finbench.driver.runtime.coordination.DummyCompletionTimeWriter;
import org.ldbcouncil.finbench.driver.runtime.metrics.MetricsService;
import org.ldbcouncil.finbench.driver.runtime.scheduling.CtDependencyCheck;
import org.ldbcouncil.finbench.driver.runtime.scheduling.Spinner;
import org.ldbcouncil.finbench.driver.temporal.TimeSource;

// TODO test
class OperationHandlerRunnableContextRetriever {
    private static final CompletionTimeWriter DUMMY_COMPLETION_TIME_WRITER = new DummyCompletionTimeWriter();
    private final Db db;
    private final CompletionTimeWriter completionTimeWriter;
    private final Spinner spinner;
    private final TimeSource timeSource;
    private final ConcurrentErrorReporter errorReporter;
    private final MetricsService metricsService;
    private final Set<Class<? extends Operation>> dependencyOperationTypes;
    private final Set<Class<? extends Operation>> dependentOperationTypes;
    private final CtDependencyCheck ctDependencyCheck;

    OperationHandlerRunnableContextRetriever(
        WorkloadStreams.WorkloadStreamDefinition streamDefinition,
        Db db,
        CompletionTimeWriter completionTimeWriter,
        CompletionTimeReader completionTimeReader,
        Spinner spinner,
        TimeSource timeSource,
        ConcurrentErrorReporter errorReporter,
        MetricsService metricsService) {
        this.db = db;
        this.completionTimeWriter = completionTimeWriter;
        this.spinner = spinner;
        this.timeSource = timeSource;
        this.errorReporter = errorReporter;
        this.metricsService = metricsService;
        this.dependentOperationTypes = streamDefinition.dependentOperationTypes();
        this.dependencyOperationTypes = streamDefinition.dependencyOperationTypes();
        this.ctDependencyCheck = new CtDependencyCheck(completionTimeReader, errorReporter);
    }

    OperationHandlerRunnableContext getInitializedHandlerFor(Operation operation)
        throws OperationExecutorException, CompletionTimeException, DbException {
        OperationHandlerRunnableContext operationHandlerRunnableContext;
        try {
            operationHandlerRunnableContext = db.getOperationHandlerRunnableContext(operation);
        } catch (Exception e) {
            throw new OperationExecutorException(
                format("Error while retrieving handler for operation\nOperation: %s", operation), e);
        }
        CompletionTimeWriter completionTimeWriterForHandler;
        // TODO this should really be a Set<Integer> --> even PrimitiveIntSet
        if (dependencyOperationTypes.contains(operation.getClass())) {
            completionTimeWriterForHandler = completionTimeWriter;
        } else {
            completionTimeWriterForHandler = DUMMY_COMPLETION_TIME_WRITER;
        }
        try {
            operationHandlerRunnableContext.init(
                timeSource,
                spinner,
                operation,
                completionTimeWriterForHandler,
                errorReporter,
                metricsService
            );
        } catch (Exception e) {
            throw new OperationExecutorException(format("Error initializing handler for: %s", operation), e);
        }
        // TODO this should really be a Set<Integer> --> even PrimitiveIntSet
        if (dependentOperationTypes.contains(operation.getClass())) {
            operationHandlerRunnableContext.setBeforeExecuteCheck(ctDependencyCheck);
        }
        return operationHandlerRunnableContext;
    }
}
