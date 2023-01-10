package org.ldbcouncil.finbench.driver.workloads;

import static java.lang.String.format;

import org.junit.jupiter.api.Test;
import org.ldbcouncil.finbench.driver.InstantiatingOperationHandlerRunnerFactory;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.OperationException;
import org.ldbcouncil.finbench.driver.OperationHandlerRunnableContext;
import org.ldbcouncil.finbench.driver.OperationHandlerRunnerFactory;
import org.ldbcouncil.finbench.driver.PoolingOperationHandlerRunnerFactory;
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeWriter;
import org.ldbcouncil.finbench.driver.runtime.coordination.DummyCompletionTimeWriter;
import org.ldbcouncil.finbench.driver.runtime.metrics.DummyCountingMetricsService;
import org.ldbcouncil.finbench.driver.runtime.metrics.MetricsService;
import org.ldbcouncil.finbench.driver.runtime.scheduling.Spinner;
import org.ldbcouncil.finbench.driver.temporal.SystemTimeSource;
import org.ldbcouncil.finbench.driver.temporal.TimeSource;
import org.ldbcouncil.finbench.driver.workloads.dummy.NothingOperation;

public class OperationHandlerRunnableContextFactoryTest {
    @Test
    public void shouldRunOperationHandlerTest() throws OperationException, InterruptedException {
        Operation operation = new NothingOperation();
        int count = 100;
        while (count < 10000000) {
            OperationHandlerRunnerFactory instantiatingOperationHandlerRunnerFactory =
                new InstantiatingOperationHandlerRunnerFactory();
            OperationHandlerRunnerFactory pooledInstantiatingOperationHandlerRunnerFactory =
                new PoolingOperationHandlerRunnerFactory(new InstantiatingOperationHandlerRunnerFactory());
            long instantiatingDuration =
                doOperationHandlerTest(count, instantiatingOperationHandlerRunnerFactory, operation);
            long pooledInstantiatingDuration =
                doOperationHandlerTest(count, pooledInstantiatingOperationHandlerRunnerFactory, operation);
            count = count * 4;
            System.out.println(format("Count: %s, Instantiating: %s, PooledInstantiating: %s, Speedup: %s", count,
                instantiatingDuration, pooledInstantiatingDuration,
                instantiatingDuration / (double) pooledInstantiatingDuration));
            instantiatingOperationHandlerRunnerFactory.shutdown();
            pooledInstantiatingOperationHandlerRunnerFactory.shutdown();
        }
    }

    public long doOperationHandlerTest(int count, OperationHandlerRunnerFactory operationHandlerRunnerFactory,
                                       Operation operation) throws OperationException {
        boolean ignoreScheduledStartTime = false;
        TimeSource timeSource = new SystemTimeSource();
        ConcurrentErrorReporter errorReporter = new ConcurrentErrorReporter();
        long spinnerSleepDuration = 0;
        Spinner spinner = new Spinner(timeSource, spinnerSleepDuration, ignoreScheduledStartTime);
        CompletionTimeWriter completionTimeWriter = new DummyCompletionTimeWriter();
        MetricsService metricsService = new DummyCountingMetricsService();
        long startTime = timeSource.nowAsMilli();
        for (int i = 0; i < count; i++) {
            OperationHandlerRunnableContext operationHandler =
                operationHandlerRunnerFactory.newOperationHandlerRunner();
            operationHandler
                .init(timeSource, spinner, operation, completionTimeWriter, errorReporter, metricsService);
            operationHandler.cleanup();
        }
        return timeSource.nowAsMilli() - startTime;
    }
}
