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


import org.ldbcouncil.finbench.driver.Operation;

public interface OperationExecutor {
    /**
     * @param operation
     * @return
     */
    void execute(Operation operation) throws OperationExecutorException;

    /**
     * Returns after executor has completed shutting down
     *
     * @param waitAsMilli duration to wait for all running operation handlers to complete execution
     * @throws OperationExecutorException
     */
    void shutdown(long waitAsMilli) throws OperationExecutorException;

    long uncompletedOperationHandlerCount();
}
