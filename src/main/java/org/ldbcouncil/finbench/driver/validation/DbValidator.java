package org.ldbcouncil.finbench.driver.validation;

import static java.lang.String.format;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.ldbcouncil.finbench.driver.Db;
import org.ldbcouncil.finbench.driver.DbConnectionState;
import org.ldbcouncil.finbench.driver.DbException;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.OperationHandler;
import org.ldbcouncil.finbench.driver.OperationHandlerRunnableContext;
import org.ldbcouncil.finbench.driver.ResultReporter;
import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.result.Path;
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead5Result;

public class DbValidator {
    /**
     * Validate the database using generated validation parameters.
     *
     * @param validationParameters  Iterator of validation parameters created using 'create_validation' mode
     * @param db                    The database connector
     * @param validationParamsCount Total validation parameters
     * @param workload              The workload to use, e.g. @see org.ldbcouncil.finbench.driver.workloads
     *                              .interactive.LdbcSnbInteractiveWorkload
     * @return DbValidationResult
     * @throws WorkloadException
     */
    public DbValidationResult validate(ListIterator<ValidationParam> validationParameters,
                                       Db db,
                                       int validationParamsCount,
                                       Workload workload) throws WorkloadException {
        System.out.println("----");
        DecimalFormat numberFormat = new DecimalFormat("###,###,###,###,###");
        DbValidationResult dbValidationResult = new DbValidationResult(db);
        ConcurrentErrorReporter errorReporter = new ConcurrentErrorReporter();
        ResultReporter resultReporter = new ResultReporter.SimpleResultReporter(errorReporter);

        Set<Class> operationMap = workload.enabledValidationOperations();

        int validationParamsProcessedSoFar = 0;
        int validationParamsCrashedSoFar = 0;
        int validationParamsIncorrectSoFar = 0;
        int validationParamsSkippedSoFar = 0;

        // Results of complex read5 are in a fixed order.
        boolean lastQueryComplexRead5 = false;
        List<Path> batchComplexRead5Results;

        Operation operation = null;
        while (true) {
            if (null != operation) {
                System.out.println(format(
                    "Processed %s / %s -- Crashed %s -- Incorrect %s -- Skipped %s -- Currently processing %s...",
                    numberFormat.format(validationParamsProcessedSoFar),
                    numberFormat.format(validationParamsCount),
                    numberFormat.format(validationParamsCrashedSoFar),
                    numberFormat.format(validationParamsIncorrectSoFar),
                    numberFormat.format(validationParamsSkippedSoFar),
                    operation.getClass().getSimpleName()
                ));
                System.out.flush();
            }

            if (!validationParameters.hasNext()) {
                break;
            }

            ValidationParam validationParam = validationParameters.next();
            operation = validationParam.operation();
            Object expectedOperationResult = validationParam.operationResult();

            if (!operationMap.contains(operation.getClass())) {
                // Skip disabled operation
                validationParamsSkippedSoFar++;
                continue;
            }

            OperationHandlerRunnableContext handlerRunner;
            try {
                handlerRunner = db.getOperationHandlerRunnableContext(operation);
            } catch (Throwable e) {

                dbValidationResult.reportMissingHandlerForOperation(operation);
                continue;
            }

            try {
                OperationHandler handler = handlerRunner.operationHandler();
                DbConnectionState dbConnectionState = handlerRunner.dbConnectionState();
                handler.executeOperation(operation, dbConnectionState, resultReporter);
                if (null == resultReporter.result()) {
                    throw new DbException(
                        format("Db returned null result for: %s", operation.getClass().getSimpleName()));
                }
            } catch (Throwable e) {
                // Not necessary, but perhaps useful for debugging
                e.printStackTrace();
                validationParamsCrashedSoFar++;
                dbValidationResult
                    .reportUnableToExecuteOperation(operation, ConcurrentErrorReporter.stackTraceToString(e));
                continue;
            } finally {
                validationParamsProcessedSoFar++;
                handlerRunner.cleanup();
            }

            Object actualOperationResult = resultReporter.result();

            // Result of ComplexRead5 may not be in the same order
            // Check this @Bingtong
            if (operation.type() == ComplexRead5.TYPE) {
                if (!lastQueryComplexRead5) {
                    lastQueryComplexRead5 = true;
                    batchComplexRead5Results = prefetchComplexRead5Results(validationParam, validationParameters);
                }
                ComplexRead5Result actual = (ComplexRead5Result) actualOperationResult;
                // Override the path equals method
                if (!batchComplexRead5Results.contains(actual.getPath())) {
                    validationParamsIncorrectSoFar++;
                    dbValidationResult.reportIncorrectResultForOperation(operation, expectedOperationResult,
                                                                         actualOperationResult);
                    continue;
                }
            } else {
                lastQueryComplexRead5 = false;
                if (!actualOperationResult.equals(expectedOperationResult)) {
                    validationParamsIncorrectSoFar++;
                    dbValidationResult
                        .reportIncorrectResultForOperation(operation, expectedOperationResult, actualOperationResult);
                    continue;
                }
            }

            dbValidationResult.reportSuccessfulExecution(operation);
        }
        System.out.println("\n----");
        return dbValidationResult;
    }

    List<Path> prefetchComplexRead5Results(ValidationParam current,
                                           ListIterator<ValidationParam> validationParameters) {
        List<Path> batch = new ArrayList<>();
        ComplexRead5Result result = (ComplexRead5Result) current.operationResult();
        batch.add(result.getPath());
        ValidationParam next = null;
        while (validationParameters.hasNext()) {
            next = validationParameters.next();
            if (next.operation().type() == ComplexRead5.TYPE) {
                result = (ComplexRead5Result) next.operationResult();
                batch.add(result.getPath());
            } else {
                validationParameters.previous(); // go back
                break;
            }
        }
        return batch;
    }
}
