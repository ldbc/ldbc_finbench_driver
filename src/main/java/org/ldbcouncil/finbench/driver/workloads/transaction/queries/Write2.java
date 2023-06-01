package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 2:
 * -- Add a Company Node --
 * Add a Company node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write2 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1002;
    public static final String COMPANY_ID = "companyId";
    public static final String COMPANY_NAME = "companyName";
    public static final String IS_BLOCKED = "isBlocked";
    private final long companyId;
    private final String companyName;
    private final boolean isBlocked;

    public Write2(@JsonProperty(COMPANY_ID) long companyId,
                  @JsonProperty(COMPANY_NAME) String companyName,
                  @JsonProperty(IS_BLOCKED) boolean isBlocked) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.isBlocked = isBlocked;
    }

    public Write2(Write2 operation) {
        this.companyId = operation.companyId;
        this.companyName = operation.companyName;
        this.isBlocked = operation.isBlocked;
    }

    @Override
    public Write2 newInstance() {
        return new Write2(this);
    }

    public long getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(COMPANY_ID, companyId)
            .put(COMPANY_NAME, companyName)
            .put(IS_BLOCKED, isBlocked)
            .build();
    }

    @Override
    public LdbcNoResult deserializeResult(String serializedResults) {
        return LdbcNoResult.INSTANCE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Write2 that = (Write2) o;
        return companyId == that.companyId
            && Objects.equals(companyName, that.companyName)
            && isBlocked == that.isBlocked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, companyName, isBlocked);
    }

    @Override
    public String toString() {
        return "Write2{"
            + "companyId="
            + companyId
            + ", companyName="
            + companyName
            + ", isBlocked="
            + isBlocked
            + '}';
    }
}

