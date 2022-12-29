package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 11:
 * -- Final share analysis in investor relationship --
 * TODO: add description
 */

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

// TODO: implement this
public class ComplexRead11 extends Operation<List<ComplexRead11Result>> {
    public static final int TYPE = 11;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public List<ComplexRead11Result> deserializeResult(String serializedOperationResult) throws IOException {
        return null;
    }
}
