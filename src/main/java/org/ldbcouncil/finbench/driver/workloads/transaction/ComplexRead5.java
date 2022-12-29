package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 5:
 * -- Exact Account Transfer Trace --
 * TODO: add description
 */

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

// TODO: implement this
public class ComplexRead5 extends Operation<List<ComplexRead5Result>> {
    public static final int TYPE = 5;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<ComplexRead5Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
