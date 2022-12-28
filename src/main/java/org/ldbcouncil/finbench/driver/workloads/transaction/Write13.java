
package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload write query 13:
 * -- Remove an account --
 * TODO: add description
 */

import org.ldbcouncil.finbench.driver.Operation;

import java.util.Map;

// TODO: implement this
public class Write13 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1013;

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
