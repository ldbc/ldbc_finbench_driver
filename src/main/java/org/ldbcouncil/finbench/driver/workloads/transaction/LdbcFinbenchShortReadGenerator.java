package org.ldbcouncil.finbench.driver.workloads.transaction;

import org.ldbcouncil.finbench.driver.ChildOperationGenerator;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.WorkloadException;

public class LdbcFinbenchShortReadGenerator implements ChildOperationGenerator
{
    @Override
    public double initialState() {
        return 0;
    }

    @Override
    public Operation nextOperation(double state, Operation operation, Object result, long actualStartTimeAsMilli,
                                   long runDurationAsNano) throws WorkloadException {
        return null;
    }

    @Override
    public double updateState(double previousState, int previousOperationType) {
        return 0;
    }
    //TODO add new short read rule
}
