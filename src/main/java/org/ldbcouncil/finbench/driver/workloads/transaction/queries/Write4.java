package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 4:
 * -- Add an Account Node owned by Person --
 * Add an *Account* node and an *own* edge from *Person* to the *Account* node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write4 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1004;
    public static final String PERSON_ID = "personId";
    public static final String ACCOUNT_ID = "accountId";
    public static final String TIME = "time";
    public static final String ACCOUNT_BLOCKED = "accountBlocked";
    public static final String ACCOUNT_TYPE = "accountType";
    private final long personId;
    private final long accountId;
    private final Date time;
    private final boolean accountBlocked;
    private final String accountType;

    public Write4(@JsonProperty(PERSON_ID) long personId,
                  @JsonProperty(ACCOUNT_ID) long accountId,
                  @JsonProperty(TIME) Date time,
                  @JsonProperty(ACCOUNT_BLOCKED) boolean accountBlocked,
                  @JsonProperty(ACCOUNT_TYPE) String accountType) {
        this.personId = personId;
        this.accountId = accountId;
        this.time = time;
        this.accountBlocked = accountBlocked;
        this.accountType = accountType;
    }

    public Write4(Write4 operation) {
        this.personId = operation.personId;
        this.accountId = operation.accountId;
        this.time = operation.time;
        this.accountBlocked = operation.accountBlocked;
        this.accountType = operation.accountType;
    }

    @Override
    public Write4 newInstance() {
        return new Write4(this);
    }

    public long getPersonId() {
        return personId;
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
            .put(PERSON_ID, personId)
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
        Write4 that = (Write4) o;
        return personId == that.personId
            && accountId == that.accountId
            && Objects.equals(time, that.time)
            && accountBlocked == that.accountBlocked
            && Objects.equals(accountType, that.accountType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, accountId, time, accountBlocked, accountType);
    }

    @Override
    public String toString() {
        return "Write4{"
            + "personId="
            + personId
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

