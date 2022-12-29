package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 6:
 * -- Withdrawal after Many-to-One transfer --
 * TODO: add description
 */

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

// TODO: implement this
public class ComplexRead6 extends Operation<List<ComplexRead6Result>> {
    public static final int TYPE = 6;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<ComplexRead6Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
