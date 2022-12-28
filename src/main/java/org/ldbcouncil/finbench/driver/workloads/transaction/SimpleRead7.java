package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload simple read query 7:
 * -- Accounts with the same transfer sources of exact account --
 * TODO: add description
 */

import org.ldbcouncil.finbench.driver.Operation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// TODO: implement this
public class SimpleRead7 extends Operation<List<SimpleRead7Result>> {
    public static final int TYPE = 107;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<SimpleRead7Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
