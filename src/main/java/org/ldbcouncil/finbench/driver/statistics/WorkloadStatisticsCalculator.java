package org.ldbcouncil.finbench.driver.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ldbcouncil.finbench.driver.ChildOperationGenerator;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.WorkloadStreams;
import org.ldbcouncil.finbench.driver.generator.GeneratorFactory;
import org.ldbcouncil.finbench.driver.generator.RandomDataGeneratorFactory;
import org.ldbcouncil.finbench.driver.runtime.metrics.ContinuousMetricManager;
import org.ldbcouncil.finbench.driver.runtime.metrics.MetricsCollectionException;
import org.ldbcouncil.finbench.driver.util.Bucket;
import org.ldbcouncil.finbench.driver.util.Histogram;

public class WorkloadStatisticsCalculator {
    /**
     * TODO report how frequently CT is updated
     */

    public WorkloadStatistics calculate(
        WorkloadStreams workloadStreams,
        long maxExpectedInterleaveAsMilli) throws MetricsCollectionException {
        Histogram<Class, Long> operationMixHistogram = new Histogram<>(0L);
        GeneratorFactory gf = new GeneratorFactory(new RandomDataGeneratorFactory(42L));
        ContinuousMetricManager operationInterleaves =
            new ContinuousMetricManager(null, null, maxExpectedInterleaveAsMilli, 5);

        long previousOperationStartTimeAsMilli = -1;

        final Map<Class, Long> previousOperationStartTimesAsMilliByOperationType = new HashMap<>();
        Map<Class, ContinuousMetricManager> operationInterleavesByOperationType = new HashMap<>();

        Map<Class, Long> firstStartTimesAsMilliByOperationType = new HashMap<>();
        Map<Class, Long> lastStartTimesAsMilliByOperationType = new HashMap<>();

        final Set<Class> dependencyOperationTypes = new HashSet<>();
        final Set<Class> dependentOperationTypes = new HashSet<>();

        // If there are no operations in the stream (e.g. they are all disabled) there is no point tracking the
        // depend operations
        if (workloadStreams.asynchronousStream().dependencyOperations().hasNext()
            || workloadStreams.asynchronousStream().nonDependencyOperations().hasNext()) {
            dependentOperationTypes.addAll(workloadStreams.asynchronousStream().dependentOperationTypes());
            dependencyOperationTypes.addAll(workloadStreams.asynchronousStream().dependencyOperationTypes());
        }

        List<Iterator<Operation>> operationIterators = new ArrayList<>();
        operationIterators.add(
            new StreamWithChildOperationGenerator(workloadStreams.asynchronousStream().dependencyOperations(),
                workloadStreams.asynchronousStream().childOperationGenerator())
        );
        operationIterators.add(
            new StreamWithChildOperationGenerator(workloadStreams.asynchronousStream().nonDependencyOperations(),
                workloadStreams.asynchronousStream().childOperationGenerator())
        );

        Iterator<Operation> operations = gf.mergeSortOperationsByScheduledStartTime(
            operationIterators.toArray(new Iterator[operationIterators.size()]));

        Map<Class, Long> lowestDependencyDurationAsMilliByOperationType = new HashMap<>();

        while (operations.hasNext()) {
            Operation operation = operations.next();
            Class operationType = operation.getClass();
            long operationStartTimeAsMilli = operation.scheduledStartTimeAsMilli();
            long operationDependencyTimeAsMilli = operation.dependencyTimeStamp();
            long operationDependencyDurationAsMilli = operationStartTimeAsMilli - operationDependencyTimeAsMilli;

            // Operation Mix
            operationMixHistogram.incOrCreateBucket(Bucket.DiscreteBucket.create(operationType), 1L);

            // Interleaves
            if (-1 != previousOperationStartTimeAsMilli) {
                long interleaveDurationAsMilli = operationStartTimeAsMilli - previousOperationStartTimeAsMilli;
                operationInterleaves.addMeasurement(interleaveDurationAsMilli);
            }
            previousOperationStartTimeAsMilli = operationStartTimeAsMilli;

            // Interleaves by operation type
            ContinuousMetricManager operationInterleaveForOperationType =
                operationInterleavesByOperationType.get(operationType);
            if (null == operationInterleaveForOperationType) {
                operationInterleaveForOperationType =
                    new ContinuousMetricManager(null, null, maxExpectedInterleaveAsMilli, 5);
                operationInterleavesByOperationType.put(operationType, operationInterleaveForOperationType);
            }
            Long previousOperationStartTimeAsMilliForOperationType =
                previousOperationStartTimesAsMilliByOperationType.get(operationType);
            if (null != previousOperationStartTimeAsMilliForOperationType) {
                long interleaveDurationAsMilli =
                    operationStartTimeAsMilli - previousOperationStartTimeAsMilliForOperationType;
                operationInterleaveForOperationType.addMeasurement(interleaveDurationAsMilli);
            }
            previousOperationStartTimesAsMilliByOperationType.put(operationType, operationStartTimeAsMilli);

            // Dependency duration by operation type
            long lowestDependencyDurationAsMilliForOperationType =
                (lowestDependencyDurationAsMilliByOperationType.containsKey(operationType))
                    ? lowestDependencyDurationAsMilliByOperationType.get(operationType)
                    : Long.MAX_VALUE;
            if (operationDependencyDurationAsMilli < lowestDependencyDurationAsMilliForOperationType) {
                lowestDependencyDurationAsMilliByOperationType.put(operationType, operationDependencyDurationAsMilli);
            }

            // First start times by operation type
            if (!firstStartTimesAsMilliByOperationType.containsKey(operationType)) {
                firstStartTimesAsMilliByOperationType.put(operationType, operationStartTimeAsMilli);
            }

            // Last start times by operation type
            lastStartTimesAsMilliByOperationType.put(operationType, operationStartTimeAsMilli);
        }

        return new WorkloadStatistics(
            firstStartTimesAsMilliByOperationType,
            lastStartTimesAsMilliByOperationType,
            operationMixHistogram,
            operationInterleaves,
            operationInterleavesByOperationType,
            dependencyOperationTypes,
            dependentOperationTypes,
            lowestDependencyDurationAsMilliByOperationType);
    }

    private static class StreamWithChildOperationGenerator implements Iterator<Operation> {
        private static final Object RESULT = null;
        private final Iterator<Operation> stream;
        private final ChildOperationGenerator childOperationGenerator;
        private double childOperationGeneratorState;
        private Operation nextChildOperation;

        private StreamWithChildOperationGenerator(Iterator<Operation> stream,
                                                  ChildOperationGenerator childOperationGenerator) {
            this.stream = stream;
            this.childOperationGenerator = childOperationGenerator;
            if (null != this.childOperationGenerator) {
                this.childOperationGeneratorState = this.childOperationGenerator.initialState();
            }
            this.nextChildOperation = null;
        }

        @Override
        public boolean hasNext() {
            return null != nextChildOperation || stream.hasNext();
        }

        @Override
        public Operation next() {
            Operation next = (null != nextChildOperation)
                ? nextChildOperation
                : stream.next();

            if (null != this.childOperationGenerator) {
                try {
                    nextChildOperation = childOperationGenerator
                        .nextOperation(childOperationGeneratorState, next, RESULT,
                            next.scheduledStartTimeAsMilli(), 0L);
                } catch (WorkloadException e) {
                    throw new RuntimeException("Error encountered while retrieving next child operation", e);
                }
                childOperationGeneratorState = (null == nextChildOperation)
                    ? childOperationGenerator.initialState()
                    : childOperationGenerator
                    .updateState(childOperationGeneratorState, next.type());
            }

            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
