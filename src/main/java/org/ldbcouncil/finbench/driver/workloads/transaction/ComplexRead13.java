package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 13:
 * -- Transfer to company amount statistics --
 * TODO: add description
 */

import org.ldbcouncil.finbench.driver.Operation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// TODO: implement this
public class ComplexRead13 extends Operation<List<ComplexRead13Result>> {
    public static final int TYPE = 13;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<ComplexRead13Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
