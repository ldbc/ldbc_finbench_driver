
package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload write query 9:
 * -- Loan Repay --
 * TODO: add description
 */

import org.ldbcouncil.finbench.driver.Operation;

import java.util.Map;

// TODO: implement this
public class Write9 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1009;

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
