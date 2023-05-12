package org.ldbcouncil.finbench.driver.validation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.Operation;

@JsonSerialize(using = ValidationParamSerializer.class)
@JsonDeserialize(using = ValidationParamDeserializer.class)
public class ValidationParam {
    private final Operation operation;
    private final Object operationResult;

    private ValidationParam(Operation operation, Object operationResult) {
        this.operation = operation;
        this.operationResult = operationResult;
    }

    public static <OPERATION extends Operation<RESULT>, RESULT> ValidationParam createTyped(OPERATION operation,
                                                                                            RESULT operationResult) {
        return new ValidationParam(operation, operationResult);
    }

    public static ValidationParam createUntyped(Operation operation, Object operationResult) {
        return new ValidationParam(operation, operationResult);
    }

    public Operation operation() {
        return operation;
    }

    public Object operationResult() {
        return operationResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ValidationParam that = (ValidationParam) o;

        if (!Objects.equals(operation, that.operation)) {
            return false;
        }
        return Objects.equals(operationResult, that.operationResult);
    }

    @Override
    public int hashCode() {
        int result = operation != null ? operation.hashCode() : 0;
        result = 31 * result + (operationResult != null ? operationResult.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ValidationParam{" + "operation=" + operation + ", operationResult=" + operationResult + '}';
    }
}
