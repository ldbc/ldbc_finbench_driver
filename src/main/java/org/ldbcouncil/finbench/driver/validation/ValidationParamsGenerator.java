package org.ldbcouncil.finbench.driver.validation;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.ldbcouncil.finbench.driver.Db;
import org.ldbcouncil.finbench.driver.DbConnectionState;
import org.ldbcouncil.finbench.driver.DbException;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.OperationHandler;
import org.ldbcouncil.finbench.driver.OperationHandlerRunnableContext;
import org.ldbcouncil.finbench.driver.ResultReporter;
import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.Workload.DbValidationParametersFilter;
import org.ldbcouncil.finbench.driver.Workload.DbValidationParametersFilterResult;
import org.ldbcouncil.finbench.driver.generator.Generator;
import org.ldbcouncil.finbench.driver.generator.GeneratorException;
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;

/**
 * Class generating validation parameters for specified workload.
 * To generate the validation parameters, the following is required:
 * - A database where the data is already loaded
 * - A validation parameters filter to determine which queries needs to be part of the validation parameters
 * - An iterator with time mapped operations
 */
public class ValidationParamsGenerator extends Generator<ValidationParam> {
    private final Db db;
    private final DbValidationParametersFilter dbValidationParametersFilter;
    private final Iterator<Operation> operations;
    private final ResultReporter resultReporter;
    private final List<Operation> injectedOperations;
    private int entriesWrittenSoFar;
    private boolean needMoreValidationParameters;
    private int validationSetSize;

    public ValidationParamsGenerator(Db db,
                                     DbValidationParametersFilter dbValidationParametersFilter,
                                     Iterator<Operation> operations,
                                     int validationSetSize) {
        this.db = db;
        this.dbValidationParametersFilter = dbValidationParametersFilter;
        this.operations = operations;
        this.resultReporter = new ResultReporter.SimpleResultReporter(new ConcurrentErrorReporter());
        this.entriesWrittenSoFar = 0;
        this.needMoreValidationParameters = true;
        this.injectedOperations = new ArrayList<>();
        this.validationSetSize = validationSetSize;
    }

    public int entriesWrittenSoFar() {
        return entriesWrittenSoFar;
    }

    @Override
    protected ValidationParam doNext() throws GeneratorException {
        Operation operation;
        while (entriesWrittenSoFar < validationSetSize
            && (!injectedOperations.isEmpty() || operations.hasNext()) && needMoreValidationParameters) {
            if (injectedOperations.isEmpty()) {
                operation = operations.next();
            } else {
                operation = injectedOperations.remove(0);
            }

            if (!dbValidationParametersFilter.useOperation(operation)) {
                continue;
            }

            Object result = getOperationResult(operation);
            DbValidationParametersFilterResult dbValidationParametersFilterResult =
                dbValidationParametersFilter.useOperationAndResultForValidation(operation, result);
            injectedOperations.addAll(dbValidationParametersFilterResult.injectedOperations());

            switch (dbValidationParametersFilterResult.acceptance()) {
                case REJECT_AND_CONTINUE:
                    continue;
                case REJECT_AND_FINISH:
                    needMoreValidationParameters = false;
                    continue;
                case ACCEPT_AND_CONTINUE:
                    entriesWrittenSoFar++;
                    return ValidationParam.createUntyped(operation, result);
                case ACCEPT_AND_FINISH:
                    entriesWrittenSoFar++;
                    needMoreValidationParameters = false;
                    return ValidationParam.createUntyped(operation, result);
                default:
                    throw new GeneratorException(
                        format("Unrecognized %s value: %s",
                            Workload.DbValidationParametersFilterAcceptance.class.getSimpleName(),
                            dbValidationParametersFilterResult.acceptance().name()
                        )
                    );
            }
        }
        // ran out of operations OR validation set size has been reached
        return null;
    }

    /**
     * Get operation result from the connected database.
     *
     * @param operation Operation to get the result from
     * @return Result object
     */
    private Object getOperationResult(Operation operation) {
        OperationHandlerRunnableContext operationHandlerRunner;
        try {
            operationHandlerRunner = db.getOperationHandlerRunnableContext(operation);
        } catch (DbException e) {
            throw new GeneratorException(
                format(
                    "Error retrieving operation handler for operation\n"
                        + "Db: %s\n"
                        + "Operation: %s",
                    db.getClass().getName(), operation),
                e);
        }
        try {
            OperationHandler operationHandler = operationHandlerRunner.operationHandler();
            DbConnectionState dbConnectionState = operationHandlerRunner.dbConnectionState();
            operationHandler.executeOperation(operation, dbConnectionState, resultReporter);
        } catch (DbException e) {
            throw new GeneratorException(
                format(""
                        + "Error executing operation to retrieve validation result\n"
                        + "Db: %s\n"
                        + "Operation: %s",
                    db.getClass().getName(), operation),
                e);
        } finally {
            operationHandlerRunner.cleanup();
        }

        return resultReporter.result();
    }
}
