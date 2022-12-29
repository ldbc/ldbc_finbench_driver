package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 7:
 * -- Fast-in and Fast-out --
 * TODO: add description
 */

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

// TODO: implement this
public class ComplexRead7 extends Operation<List<ComplexRead7Result>> {
    public static final int TYPE = 7;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<ComplexRead7Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
