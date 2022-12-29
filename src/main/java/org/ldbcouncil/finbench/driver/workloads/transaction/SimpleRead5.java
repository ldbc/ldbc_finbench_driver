package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload simple read query 5:
 * -- Recent transfer-ins statistic --
 * TODO: add description
 */

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

// TODO: implement this
public class SimpleRead5 extends Operation<List<SimpleRead5Result>> {
    public static final int TYPE = 105;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<SimpleRead5Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
