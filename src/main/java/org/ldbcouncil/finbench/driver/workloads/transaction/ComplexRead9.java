package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 9:
 * -- Money laundering with loan involved --
 * TODO: add description
 */

import org.ldbcouncil.finbench.driver.Operation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// TODO: implement this
public class ComplexRead9 extends Operation<List<ComplexRead9Result>> {
    public static final int TYPE = 9;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<ComplexRead9Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
