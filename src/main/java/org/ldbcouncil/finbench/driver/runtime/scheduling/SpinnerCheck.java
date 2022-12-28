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
