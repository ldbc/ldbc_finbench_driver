package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload simple read query 3:
 * -- Company-related information --
 * TODO: add description
 */

import org.ldbcouncil.finbench.driver.Operation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// TODO: implement this
public class SimpleRead3 extends Operation<List<SimpleRead3Result>> {
    public static final int TYPE = 103;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<SimpleRead3Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
