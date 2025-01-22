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

package org.ldbcouncil.finbench.driver.runtime.scheduling;

import org.ldbcouncil.finbench.driver.Operation;

public interface SpinnerCheck {
    enum SpinnerCheckResult {
        PASSED,
        STILL_CHECKING,
        FAILED
    }

    /**
     * Once a check has returned true it may never again return false
     *
     * @return
     */
    SpinnerCheckResult doCheck(Operation operation);


    /**
     * Only called if check fails
     * Return value dictates if operation may still be executed, or if execution should be aborted.
     *
     * @param operation
     * @return operation may still be executed
     */
    boolean handleFailedCheck(Operation operation);
}
