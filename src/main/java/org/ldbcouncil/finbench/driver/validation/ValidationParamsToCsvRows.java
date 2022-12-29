package org.ldbcouncil.finbench.driver.validation;

import static java.lang.String.format;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.generator.GeneratorException;

public class ValidationParamsToCsvRows implements Iterator<String[]> {
    private final Iterator<ValidationParam> validationParams;
    private final Workload workload;
    private final boolean performSerializationMarshallingChecks;
    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public ValidationParamsToCsvRows(Iterator<ValidationParam> validationParams,
                                     Workload workload,
                                     boolean performSerializationMarshallingChecks) {
        this.validationParams = validationParams;
        this.workload = workload;
        this.performSerializationMarshallingChecks = performSerializationMarshallingChecks;
    }

    @Override
    public boolean hasNext() {
        return validationParams.hasNext();
    }

    @Override
    public String[] next() {
        ValidationParam validationParam = validationParams.next();
        Operation operation = validationParam.operation();
        Object operationResult = validationParam.operationResult();

        String serializedOperation;
        try {
            serializedOperation = OBJECT_MAPPER.writeValueAsString(operation);
        } catch (IOException e) {
            throw new GeneratorException(
                format(
                    "Workload(%s) unable to serialize operation\n"
                        + "Operation: %s",
                    operation),
                e);
        }

        String serializedOperationResult;
        try {
            serializedOperationResult = OBJECT_MAPPER.writeValueAsString(operationResult);
        } catch (JsonProcessingException e) {
            throw new GeneratorException(
                format(
                    "Error serializing operation result\n"
                        + "Operation: %s\n"
                        + "Operation Result: %s",
                    operation, operationResult),
                e);
        }

        // Assert that serialization/marshalling is performed correctly
        if (performSerializationMarshallingChecks) {
            Object marshaledOperationResult = null;
            try {
                marshaledOperationResult = operation.deserializeResult(serializedOperationResult);
            } catch (IOException e) {
                throw new GeneratorException(
                    format(""
                            + "Error marshalling serialized operation result\n"
                            + "Operation: %s\n"
                            + "Operation Result: %s\n"
                            + "Serialized Result: %s",
                        operation, operationResult, serializedOperationResult),
                    e);
            }
            if (!marshaledOperationResult.equals(operationResult)) {
                throw new GeneratorException(
                    format(""
                            + "Operation result and serialized-then-marshaled operation result do not equal\n"
                            + "Operation: %s\n"
                            + "Actual Result: %s\n"
                            + "Serialized Result: %s\n"
                            + "Marshaled Result: %s",
                        operation, operationResult, serializedOperationResult, marshaledOperationResult)
                );
            }
        }

        return new String[] {serializedOperation, serializedOperationResult};
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() not supported by " + getClass().getName());
    }
}
