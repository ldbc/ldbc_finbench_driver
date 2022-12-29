package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload write query 10:
 * -- Block an account of high risk --
 * TODO: add description
 */

import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

// TODO: implement this
public class Write10 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1010;

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return null;
    }

    @Override
    public LdbcNoResult deserializeResult(String serializedResults) {
        return LdbcNoResult.INSTANCE;
    }
}
