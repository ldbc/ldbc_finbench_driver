package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 8:
 * -- Transfer trace after loan applied --
 * TODO: add description
 */

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

// TODO: implement this
public class ComplexRead8 extends Operation<List<ComplexRead8Result>> {
    public static final int TYPE = 8;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<ComplexRead8Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
