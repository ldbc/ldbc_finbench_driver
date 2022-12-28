package org.ldbcouncil.finbench.driver.validation;

import org.ldbcouncil.finbench.driver.*;
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcFinBenchTransactionWorkloadConfiguration;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;

import static java.lang.String.format;

public class DbValidator {
    /**
     * Validate the database using generated validation parameters.
     *
     * @param validationParameters  Iterator of validation parameters created using 'create_validation' mode
     * @param db                    The database connector
     * @param validationParamsCount Total validation parameters
     * @param workload              The workload to use, e.g. @see org.ldbcouncil.finbench.driver.workloads.interactive.LdbcSnbInteractiveWorkload
     * @return
     * @throws WorkloadException
     */
    public DbValidationResult validate(Iterator<ValidationParam> validationParameters,
                                       Db db,
                                       int validationParamsCount,
                                       Workload workload) throws WorkloadException {
        System.out.println("----");
        DecimalFormat numberFormat = new DecimalFormat("###,###,###,###,###");
        DbValidationResult dbValidationResult = new DbValidationResult(db);
        ConcurrentErrorReporter errorReporter = new ConcurrentErrorReporter();
        ResultReporter resultReporter = new ResultReporter.SimpleResultReporter(errorReporter);

        Map<Integer, Class<? extends Operation>> operationMap =
                LdbcFinBenchTransactionWorkloadConfiguration.operationTypeToClassMapping();

        int validationParamsProcessedSoFar = 0;
        int validationParamsCrashedSoFar = 0;
        int validationParamsIncorrectSoFar = 0;

        Operation operation = null;
        while (true) {
            if (null != operation) {
                System.out.print(format(
                        "Processed %s / %s -- Crashed %s -- Incorrect %s -- Currently processing %s...\r",
                        numberFormat.format(validationParamsProcessedSoFar),
                        numberFormat.format(validationParamsCount),
                        numberFormat.format(validationParamsCrashedSoFar),
                        numberFormat.format(validationParamsIncorrectSoFar),
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

//            // Exception for Q14 where the path ordering for equal weights is not defined.
//            // This comparison should be made on list level and then on individual paths
//            // where paths with equal weights are grouped and compared.
//            // TODO: Either remove workload abstraction or move this to separate validator class.
//            if (LdbcQuery14.class  == operationMap.get(operation.type()))
//            {
//                if (!LdbcQuery14Result.resultListEqual(expectedOperationResult, actualOperationResult)){
//                    validationParamsIncorrectSoFar++;
//                    dbValidationResult
//                            .reportIncorrectResultForOperation( operation, expectedOperationResult, actualOperationResult );
//                    continue;
//                }
//            }
//
//            else if ( false == actualOperationResult.equals(expectedOperationResult))
//            {
//                validationParamsIncorrectSoFar++;
//                dbValidationResult
//                        .reportIncorrectResultForOperation( operation, expectedOperationResult, actualOperationResult );
//                continue;
//            }

            dbValidationResult.reportSuccessfulExecution(operation);
        }
        System.out.println("\n----");
        return dbValidationResult;
    }
}
