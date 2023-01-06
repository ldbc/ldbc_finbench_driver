package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload write query 2:
 * -- Add an Account Node owned by Company --
 * Add an account node. Add a Company node and an own edge from it to the account Node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.Operation;

public class Write2 extends Operation<LdbcNoResult> {
    public static final int TYPE = 1002;
    public static final String COMPANY_ID = "companyId";
    public static final String COMPANY_NAME = "companyName";
    public static final String ACCOUNT_ID = "accountId";
    public static final String CURRENT_TIME = "currentTime";
    public static final String ACCOUNT_BLOCKED = "accountBlocked";
    public static final String ACCOUNT_TYPE = "accountType";
    private final long companyId;
    private final String companyName;
    private final long accountId;
    private final Date currentTime;
    private final boolean accountBlocked;
    private final String accountType;

    public Write2(@JsonProperty(COMPANY_ID) long companyId,
                  @JsonProperty(COMPANY_NAME) String companyName,
                  @JsonProperty(ACCOUNT_ID) long accountId,
                  @JsonProperty(CURRENT_TIME) Date currentTime,
                  @JsonProperty(ACCOUNT_BLOCKED) boolean accountBlocked,
                  @JsonProperty(ACCOUNT_TYPE) String accountType) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.accountId = accountId;
        this.currentTime = currentTime;
        this.accountBlocked = accountBlocked;
        this.accountType = accountType;
    }

    public long getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public long getAccountId() {
        return accountId;
    }

    public Date getCurrentTime() {
        return currentTime;
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
            .put(COMPANY_NAME, companyName)
            .put(ACCOUNT_ID, accountId)
            .put(CURRENT_TIME, currentTime)
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
        Write2 that = (Write2) o;
        return companyId == that.companyId
            && Objects.equals(companyName, that.companyName)
            && accountId == that.accountId
            && Objects.equals(currentTime, that.currentTime)
            && accountBlocked == that.accountBlocked
            && Objects.equals(accountType, that.accountType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, companyName, accountId, currentTime, accountBlocked, accountType);
    }

    @Override
    public String toString() {
        return "Write2{"
            + "companyId="
            + companyId
            + ", companyName="
            + companyName
            + ", accountId="
            + accountId
            + ", currentTime="
            + currentTime
            + ", accountBlocked="
            + accountBlocked
            + ", accountType="
            + accountType
            + '}';
    }
}

