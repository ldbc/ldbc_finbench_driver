package org.ldbcouncil.finbench.driver.workloads.dummy;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;

public class NothingOperation extends Operation<DummyResult> {
    public static final int TYPE = 0;

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.of();
    }

    @Override
    public boolean equals(Object that) {
        return true;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public DummyResult deserializeResult(String serializedOperationResult) {
        return new DummyResult();
    }
}
