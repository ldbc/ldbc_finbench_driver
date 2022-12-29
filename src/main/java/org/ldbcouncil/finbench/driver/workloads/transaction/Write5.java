package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload write query 5:
 * -- Add Loan applied by Person --
 * TODO: add description
 */

import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

// TODO: implement this
public class Write5 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1005;

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
