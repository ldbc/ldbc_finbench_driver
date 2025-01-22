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

package org.ldbcouncil.finbench.driver.runtime.executor;


import org.ldbcouncil.finbench.driver.ChildOperationGenerator;
import org.ldbcouncil.finbench.driver.DbException;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.OperationHandlerRunnableContext;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeException;

public class ChildOperationExecutor {
    public void execute(
        ChildOperationGenerator childOperationGenerator,
        Operation operation,
        Object result,
        long actualStartTimeAsMilli,
        long runDurationAsNano,
        OperationHandlerRunnableContextRetriever operationHandlerRunnableContextRetriever)
        throws WorkloadException, DbException, OperationExecutorException, CompletionTimeException {
        if (null == childOperationGenerator) {
            return;
        } else {
            if (null != childOperationGenerator) {
                double state = childOperationGenerator.initialState();
                operation = childOperationGenerator.nextOperation(
                    state,
                    operation,
                    result,
                    actualStartTimeAsMilli,
                    runDurationAsNano
                );
                while (null != operation) {
                    OperationHandlerRunnableContext childOperationHandlerRunnableContext =
                        operationHandlerRunnableContextRetriever.getInitializedHandlerFor(operation);
                    childOperationHandlerRunnableContext.run();
                    state = childOperationGenerator.updateState(state, operation.type());
                    operation = childOperationGenerator.nextOperation(
                        state,
                        childOperationHandlerRunnableContext.operation(),
                        childOperationHandlerRunnableContext.resultReporter().result(),
                        childOperationHandlerRunnableContext.resultReporter().actualStartTimeAsMilli(),
                        childOperationHandlerRunnableContext.resultReporter().runDurationAsNano()
                    );
                    childOperationHandlerRunnableContext.cleanup();
                }
            }
        }
    }
}
