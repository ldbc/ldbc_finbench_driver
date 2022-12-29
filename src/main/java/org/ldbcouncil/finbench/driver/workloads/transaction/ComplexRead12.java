package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 12:
 * -- Guarantee Cycle Detection --
 * TODO: add description
 */

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

// TODO: implement this
public class ComplexRead12 extends Operation<List<ComplexRead12Result>> {
    public static final int TYPE = 12;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<ComplexRead12Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
