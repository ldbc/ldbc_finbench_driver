/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ldbcouncil.finbench.driver.validation;

import static java.lang.String.format;

import java.text.DecimalFormat;
import java.util.Iterator;
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
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;

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
    public DbValidationResult validate(Iterator<ValidationParam> validationParameters,
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

            if (!actualOperationResult.equals(expectedOperationResult)) {
                validationParamsIncorrectSoFar++;
                dbValidationResult
                    .reportIncorrectResultForOperation(operation, expectedOperationResult, actualOperationResult);
                continue;
            }

            dbValidationResult.reportSuccessfulExecution(operation);
        }
        System.out.println("\n----");
        return dbValidationResult;
    }
}
