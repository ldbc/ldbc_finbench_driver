package org.ldbcouncil.finbench.driver.workloads.dummy;


import org.ldbcouncil.finbench.driver.DbConnectionState;
import org.ldbcouncil.finbench.driver.DbException;
import org.ldbcouncil.finbench.driver.OperationHandler;
import org.ldbcouncil.finbench.driver.ResultReporter;

public class NothingOperationHandler implements OperationHandler<NothingOperation, DbConnectionState> {
    @Override
    public void executeOperation(NothingOperation operation, DbConnectionState dbConnectionState,
                                 ResultReporter resultReporter) throws
        DbException {
    }
}
