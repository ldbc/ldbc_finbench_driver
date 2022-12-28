package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload simple read query 1:
 * -- Exact Account properties query --
 * TODO: add description
 */

import org.ldbcouncil.finbench.driver.Operation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// TODO: implement this
public class SimpleRead1 extends Operation<List<SimpleRead1Result>> {
    public static final int TYPE = 101;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<SimpleRead1Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
