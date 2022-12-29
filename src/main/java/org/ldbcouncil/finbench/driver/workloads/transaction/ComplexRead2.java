package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 2:
 * -- Fund gathered from the accounts applying loans --
 * TODO: add description
 */

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

// TODO: implement this
public class ComplexRead2 extends Operation<List<ComplexRead2Result>> {
    public static final int TYPE = 2;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<ComplexRead2Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
