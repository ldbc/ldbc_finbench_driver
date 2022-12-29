package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 4:
 * -- Three accounts in a transfer cycle --
 * TODO: add description
 */

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

// TODO: implement this
public class ComplexRead4 extends Operation<List<ComplexRead4Result>> {
    public static final int TYPE = 4;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<ComplexRead4Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
