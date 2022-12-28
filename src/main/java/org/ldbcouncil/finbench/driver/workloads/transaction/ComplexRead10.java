package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 10:
 * -- Similarity of investor relationship --
 * TODO: add description
 */

import org.ldbcouncil.finbench.driver.Operation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// TODO: implement this
public class ComplexRead10 extends Operation<List<ComplexRead10Result>> {
    public static final int TYPE = 10;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<ComplexRead10Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
