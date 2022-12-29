package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload simple read query 2:
 * -- Transfers between two accounts --
 * TODO: add description
 */

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

// TODO: implement this
public class SimpleRead2 extends Operation<List<SimpleRead2Result>> {
    public static final int TYPE = 102;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<SimpleRead2Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
