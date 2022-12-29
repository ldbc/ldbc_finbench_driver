package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload simple read query 4:
 * -- Account transfer-outs statistic over threshold --
 * TODO: add description
 */

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

// TODO: implement this
public class SimpleRead4 extends Operation<List<SimpleRead4Result>> {
    public static final int TYPE = 104;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<SimpleRead4Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
