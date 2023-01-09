package org.ldbcouncil.finbench.driver.workloads.dummy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.SerializingMarshallingException;
import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.WorkloadStreams;
import org.ldbcouncil.finbench.driver.generator.GeneratorFactory;

public class DummyWorkload extends Workload {
    public static final Map<Integer, Class<? extends Operation>> OPERATION_TYPE_CLASS_MAPPING =
        createOperationTypeToClassMapping();
    private final long maxExpectedInterleaveAsMilli;
    private final WorkloadStreams workloadStreams;

    public DummyWorkload(WorkloadStreams workloadStreams,
                         long maxExpectedInterleaveAsMilli) {
        this.maxExpectedInterleaveAsMilli = maxExpectedInterleaveAsMilli;
        this.workloadStreams = workloadStreams;
    }

    private static final Map<Integer, Class<? extends Operation>> createOperationTypeToClassMapping() {
        Map<Integer, Class<? extends Operation>> operationTypeToClassMapping = new HashMap<>();
        operationTypeToClassMapping.put(NothingOperation.TYPE, NothingOperation.class);
        operationTypeToClassMapping.put(TimedNamedOperation1.TYPE, TimedNamedOperation1.class);
        operationTypeToClassMapping.put(TimedNamedOperation2.TYPE, TimedNamedOperation2.class);
        operationTypeToClassMapping.put(TimedNamedOperation3.TYPE, TimedNamedOperation3.class);
        return operationTypeToClassMapping;
    }

    @Override
    public Set<Class> enabledValidationOperations() {
        return Collections.emptySet();
    }

    @Override
    public Class<? extends Operation> getOperationClass() {
        return Operation.class;
    }

    @Override
    public Map<Integer, Class<? extends Operation>> operationTypeToClassMapping() {
        return OPERATION_TYPE_CLASS_MAPPING;
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onInit(Map<String, String> params) throws WorkloadException {
    }

    @Override
    protected WorkloadStreams getStreams(GeneratorFactory generators, boolean hasDbConnected) throws WorkloadException {
        return newCopyOfWorkloadStreams();
    }

    private WorkloadStreams newCopyOfWorkloadStreams() {
        return workloadStreams;
    }

    public String serializeOperation(Operation operation) throws SerializingMarshallingException {
        if (operation.getClass().equals(NothingOperation.class)) {
            return NothingOperation.class.getName();
        }
        if (operation.getClass().equals(TimedNamedOperation1.class)) {
            return TimedNamedOperation1.class.getName()
                + "|"
                + operation.scheduledStartTimeAsMilli()
                + "|"
                + operation.timeStamp()
                + "|"
                + operation.dependencyTimeStamp()
                + "|"
                + serializeName(((TimedNamedOperation1) operation).name());
        }
        if (operation.getClass().equals(TimedNamedOperation2.class)) {
            return TimedNamedOperation2.class.getName()
                + "|"
                + operation.scheduledStartTimeAsMilli()
                + "|"
                + operation.timeStamp()
                + "|"
                + operation.dependencyTimeStamp()
                + "|"
                + serializeName(((TimedNamedOperation2) operation).name());
        }
        throw new SerializingMarshallingException("Unsupported Operation: " + operation.getClass().getName());
    }

    public Operation marshalOperation(String serializedOperation) throws SerializingMarshallingException {
        if (serializedOperation.startsWith(NothingOperation.class.getName())) {
            return new NothingOperation();
        }
        if (serializedOperation.startsWith(TimedNamedOperation1.class.getName())) {
            String[] serializedOperationTokens = serializedOperation.split("\\|");
            return new TimedNamedOperation1(
                Long.parseLong(serializedOperationTokens[1]),
                Long.parseLong(serializedOperationTokens[2]),
                Long.parseLong(serializedOperationTokens[3]),
                marshalName(serializedOperationTokens[4])
            );
        }
        if (serializedOperation.startsWith(TimedNamedOperation2.class.getName())) {
            String[] serializedOperationTokens = serializedOperation.split("\\|");
            return new TimedNamedOperation2(
                Long.parseLong(serializedOperationTokens[1]),
                Long.parseLong(serializedOperationTokens[2]),
                Long.parseLong(serializedOperationTokens[3]),
                marshalName(serializedOperationTokens[4])
            );
        }
        throw new SerializingMarshallingException("Unsupported Operation: " + serializedOperation);
    }

    public boolean resultsEqual(Operation operation, Object result1, Object result2) throws WorkloadException {
        if (null == result1 || null == result2) {
            return false;
        } else {
            return result1.equals(result2);
        }
    }

    private String serializeName(String name) {
        return (null == name) ? "null" : name;
    }

    private String marshalName(String nameString) {
        return ("null".equals(nameString)) ? null : nameString;
    }

    @Override
    public long maxExpectedInterleaveAsMilli() {
        return maxExpectedInterleaveAsMilli;
    }
}
