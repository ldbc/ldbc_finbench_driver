package org.ldbcouncil.finbench.driver.workloads.transaction;
/**
 * LdbcNoResult.java
 * <p>
 * This class defines the results from the insert operations (update queries)
 */

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = LdbcNoResultJsonSerializer.class)
public class LdbcNoResult {
    public static final LdbcNoResult INSTANCE = new LdbcNoResult();

    private LdbcNoResult() {
    }
}
