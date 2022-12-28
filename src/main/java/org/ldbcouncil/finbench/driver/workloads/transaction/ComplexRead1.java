package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 1:
 * -- Blocked medium related accounts --
 * TODO: add description
 */

import org.ldbcouncil.finbench.driver.Operation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// TODO: implement this
public class ComplexRead1 extends Operation<List<ComplexRead1Result>> {
    public static final int TYPE = 1;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<ComplexRead1Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
