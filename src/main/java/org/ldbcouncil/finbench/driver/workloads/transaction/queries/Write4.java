/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    public static final String COMMENT = "comment";
    public static final String ACCOUNT_BLOCKED = "accountBlocked";
    public static final String ACCOUNT_TYPE = "accountType";
    public static final String ACCOUNT_NICKNAME = "nickname";
    public static final String ACCOUNT_PHONENUM = "phoneNum";
    public static final String ACCOUNT_EMAIL = "email";
    public static final String ACCOUNT_FREQ_LOGIN_TYPE = "freqLoginType";
    public static final String ACCOUNT_LAST_LOGIN_TIME = "lastLoginTime";
    public static final String ACCOUNT_LEVEL = "level";
    private final long personId;
    private final long accountId;
    private final Date time;
    private final String comment;
    private final boolean accountBlocked;
    private final String accountType;
    private final String accountNickname;
    private final String accountPhoneNum;
    private final String accountEmail;
    private final String accountFreqLoginType;
    private final long accountLastLoginTime;
    private final String accountLevel;

    public Write4(@JsonProperty(PERSON_ID) long personId,
                  @JsonProperty(ACCOUNT_ID) long accountId,
                  @JsonProperty(TIME) Date time,
                  @JsonProperty(COMMENT) String comment,
                  @JsonProperty(ACCOUNT_BLOCKED) boolean accountBlocked,
                  @JsonProperty(ACCOUNT_TYPE) String accountType,
                  @JsonProperty(ACCOUNT_NICKNAME) String accountNickname,
                  @JsonProperty(ACCOUNT_PHONENUM) String accountPhoneNum,
                  @JsonProperty(ACCOUNT_EMAIL) String accountEmail,
                  @JsonProperty(ACCOUNT_FREQ_LOGIN_TYPE) String accountFreqLoginType,
                  @JsonProperty(ACCOUNT_LAST_LOGIN_TIME) long accountLastLoginTime,
                  @JsonProperty(ACCOUNT_LEVEL) String accountLevel) {
        this.personId = personId;
        this.accountId = accountId;
        this.time = time;
        this.comment = comment;
        this.accountBlocked = accountBlocked;
        this.accountType = accountType;
        this.accountNickname = accountNickname;
        this.accountPhoneNum = accountPhoneNum;
        this.accountEmail = accountEmail;
        this.accountFreqLoginType = accountFreqLoginType;
        this.accountLastLoginTime = accountLastLoginTime;
        this.accountLevel = accountLevel;
    }

    public Write4(Write4 operation) {
        this.personId = operation.personId;
        this.accountId = operation.accountId;
        this.time = operation.time;
        this.comment = operation.comment;
        this.accountBlocked = operation.accountBlocked;
        this.accountType = operation.accountType;
        this.accountNickname = operation.accountNickname;
        this.accountPhoneNum = operation.accountPhoneNum;
        this.accountEmail = operation.accountEmail;
        this.accountFreqLoginType = operation.accountFreqLoginType;
        this.accountLastLoginTime = operation.accountLastLoginTime;
        this.accountLevel = operation.accountLevel;
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
        return Objects.hash(personId, accountId, time.getTime(), accountBlocked, accountType);
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

