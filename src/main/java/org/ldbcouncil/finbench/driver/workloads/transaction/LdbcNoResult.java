package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/*
 * This class defines the results from the insert operations (update queries)
 */
@JsonSerialize(using = LdbcNoResultJsonSerializer.class)
public class LdbcNoResult {
    public static final LdbcNoResult INSTANCE = new LdbcNoResult();

    private LdbcNoResult() {
    }
}
