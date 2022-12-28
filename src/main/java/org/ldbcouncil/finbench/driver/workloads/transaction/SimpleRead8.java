package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload simple read query 8:
 * -- Many-to-one blocked account monitoring --
 * TODO: add description
 */

import org.ldbcouncil.finbench.driver.Operation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// TODO: implement this
public class SimpleRead8 extends Operation<List<SimpleRead8Result>> {
    public static final int TYPE = 108;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<SimpleRead8Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
