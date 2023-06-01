package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 5:
 * --  Add an Account Node owned by Company --
 * Add an *Account* node and an *own* edge from *Company* to the *Account* node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write5 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1005;
    public static final String COMPANY_ID = "companyId";
    public static final String ACCOUNT_ID = "accountId";
    public static final String TIME = "time";
    public static final String ACCOUNT_BLOCKED = "accountBlocked";
    public static final String ACCOUNT_TYPE = "accountType";
    private final long companyId;
    private final long accountId;
    private final Date time;
    private final boolean accountBlocked;
    private final String accountType;

    public Write5(@JsonProperty(COMPANY_ID) long companyId,
                  @JsonProperty(ACCOUNT_ID) long accountId,
                  @JsonProperty(TIME) Date time,
                  @JsonProperty(ACCOUNT_BLOCKED) boolean accountBlocked,
                  @JsonProperty(ACCOUNT_TYPE) String accountType) {
        this.companyId = companyId;
        this.accountId = accountId;
        this.time = time;
        this.accountBlocked = accountBlocked;
        this.accountType = accountType;
    }

    public Write5(Write5 operation) {
        this.companyId = operation.companyId;
        this.accountId = operation.accountId;
        this.time = operation.time;
        this.accountBlocked = operation.accountBlocked;
        this.accountType = operation.accountType;
    }

    @Override
    public Write5 newInstance() {
        return new Write5(this);
    }

    public long getCompanyId() {
        return companyId;
    }

    public long getAccountId() {
        return accountId;
    }

    public Date getTime() {
        return time;
    }

    public boolean getAccountBlocked() {
        return accountBlocked;
    }

    public String getAccountType() {
        return accountType;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(COMPANY_ID, companyId)
            .put(ACCOUNT_ID, accountId)
            .put(TIME, time)
            .put(ACCOUNT_BLOCKED, accountBlocked)
            .put(ACCOUNT_TYPE, accountType)
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
        Write5 that = (Write5) o;
        return companyId == that.companyId
            && accountId == that.accountId
            && Objects.equals(time, that.time)
            && accountBlocked == that.accountBlocked
            && Objects.equals(accountType, that.accountType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, accountId, time, accountBlocked, accountType);
    }

    @Override
    public String toString() {
        return "Write5{"
            + "companyId="
            + companyId
            + ", accountId="
            + accountId
            + ", time="
            + time
            + ", accountBlocked="
            + accountBlocked
            + ", accountType="
            + accountType
            + '}';
    }
}

