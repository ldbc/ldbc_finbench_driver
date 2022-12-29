package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload simple read query 6:
 * -- Companies with the same investor of exact company --
 * TODO: add description
 */

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

// TODO: implement this
public class SimpleRead6 extends Operation<List<SimpleRead6Result>> {
    public static final int TYPE = 106;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<SimpleRead6Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
