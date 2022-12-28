package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 3:
 * -- Shortest transfer path --
 * TODO: add description
 */

import org.ldbcouncil.finbench.driver.Operation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// TODO: implement this
public class ComplexRead3 extends Operation<List<ComplexRead3Result>> {
    public static final int TYPE = 3;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<ComplexRead3Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
