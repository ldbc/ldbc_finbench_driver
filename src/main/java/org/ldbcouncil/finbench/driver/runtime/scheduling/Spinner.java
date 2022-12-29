package org.ldbcouncil.finbench.driver.runtime.scheduling;

import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.temporal.TimeSource;
import org.ldbcouncil.finbench.driver.util.Function2;

// TODO if error policy DOES NOT terminate benchmark and DOES NOT allow the operation to complete something needs
// TODO to be done about DEPENDENT/CT, because the IT for the operation has already been reported
// TODO perhaps the CT for that operation needs to be reported too (to CT service, but not to MetricsService),
// TODO to make sure DEPENDENT/CT does not freeze at the IT of that "Failed" operation
//
// TODO * if operation is a Dependency the run must terminate, as operation may not have been completed
// TODO * if operation is not a Dependency a policy could decide if the benchmark should terminate or not
//
// TODO take boolean result from spinner into consideration, i.e., DO NOT execute handler for "Failed" operations

public class Spinner {
    public static final long DEFAULT_SLEEP_DURATION_10_MILLI = 10;
    public static final SpinnerCheck TRUE_CHECK = new TrueCheck();

    private final Function2<Operation, SpinnerCheck, Boolean, RuntimeException> spinFun;

    public Spinner(
        TimeSource timeSource,
        long sleepDurationAsMilli,
        boolean ignoreScheduleStartTimes) {
        this.spinFun = (ignoreScheduleStartTimes)
            ? new WaitForChecksFun(sleepDurationAsMilli)
            : new WaitForChecksAndScheduledStartTimeFun(timeSource, sleepDurationAsMilli);
    }

    boolean waitForScheduledStartTime(Operation operation) {
        return waitForScheduledStartTime(operation, TRUE_CHECK);
    }

    /**
     * waits for the scheduled start time of an operation, and returns boolean value indicating if operation should be
     * executed or not.
     * true  = operation may still be executed
     * false = operation should not be scheduled
     * return value calculated as follows:
     * true && handleFailedCheck
     * i.e., if error occurs it (1) has ability to cancel operation execution (2) can do anything else in its handler
     *
     * @param operation operation to wait for
     * @param check     checks that must all pass before spinner returns
     * @return operation may be executed
     */
    public boolean waitForScheduledStartTime(Operation operation, SpinnerCheck check) {
        return spinFun.apply(operation, check);
    }

    // sleep to reduce CPU load while spinning
    // NOTE: longer sleep == lower scheduling accuracy AND lower achievable throughput
    public static void powerNap(long sleepMs) {
        if (0 < sleepMs) {
            try {
                Thread.sleep(sleepMs);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }

    private static class WaitForChecksAndScheduledStartTimeFun implements
        Function2<Operation, SpinnerCheck, Boolean, RuntimeException> {
        private final TimeSource timeSource;
        private final long sleepDurationAsMilli;

        private WaitForChecksAndScheduledStartTimeFun(
            TimeSource timeSource,
            long sleepDurationAsMilli) {
            this.timeSource = timeSource;
            this.sleepDurationAsMilli = sleepDurationAsMilli;
        }

        @Override
        public Boolean apply(Operation operation, SpinnerCheck check) {
            // earliest time at which operation may start
            // wait for checks to have all passed before allowing operation to start
            while (SpinnerCheck.SpinnerCheckResult.STILL_CHECKING == check.doCheck(operation)) {
                powerNap(sleepDurationAsMilli);
            }

            // wait for scheduled operation start time
            while (timeSource.nowAsMilli() < operation.scheduledStartTimeAsMilli()) {
                powerNap(sleepDurationAsMilli);
            }

            return SpinnerCheck.SpinnerCheckResult.PASSED == check.doCheck(operation);
        }
    }

    private static class WaitForChecksFun implements Function2<Operation, SpinnerCheck, Boolean, RuntimeException> {
        private final long sleepDurationAsMilli;

        private WaitForChecksFun(long sleepDurationAsMilli) {
            this.sleepDurationAsMilli = sleepDurationAsMilli;
        }

        @Override
        public Boolean apply(Operation operation, SpinnerCheck check) {
            // wait for checks to have all passed before allowing operation to start
            while (SpinnerCheck.SpinnerCheckResult.STILL_CHECKING == check.doCheck(operation)) {
                powerNap(sleepDurationAsMilli);
            }

            return SpinnerCheck.SpinnerCheckResult.PASSED == check.doCheck(operation);
        }
    }

    private static class TrueCheck implements SpinnerCheck {
        @Override
        public SpinnerCheckResult doCheck(Operation operation) {
            return SpinnerCheckResult.PASSED;
        }

        @Override
        public boolean handleFailedCheck(Operation operation) {
            return true;
        }
    }
}
