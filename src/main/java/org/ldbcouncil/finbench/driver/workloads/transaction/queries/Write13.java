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
 * Transaction workload write query 13:
 * -- Add Withdraw Between Accounts --
 * Add a *withdraw* edge from an *Account* node to another *Account* node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write13 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1013;
    public static final String ACCOUNT_ID1 = "accountId1";
    public static final String ACCOUNT_ID2 = "accountId2";
    public static final String TIME = "time";
    public static final String AMOUNT = "amount";
    public static final String FROM_TYPE = "fromType";
    public static final String TO_TYPE = "toType";
    public static final String COMMENT = "comment";
    private final long accountId1;
    private final long accountId2;
    private final Date time;
    private final double amount;
    private final String fromType;
    private final String toType;
    private final String comment;

    public Write13(@JsonProperty(ACCOUNT_ID1) long accountId1,
                   @JsonProperty(ACCOUNT_ID2) long accountId2,
                   @JsonProperty(TIME) Date time,
                   @JsonProperty(AMOUNT) double amount,
                   @JsonProperty(FROM_TYPE) String fromType,
                   @JsonProperty(TO_TYPE) String toType,
                   @JsonProperty(COMMENT) String comment) {
        this.accountId1 = accountId1;
        this.accountId2 = accountId2;
        this.time = time;
        this.amount = amount;
        this.fromType = fromType;
        this.toType = toType;
        this.comment = comment;
    }

    public Write13(Write13 operation) {
        this.accountId1 = operation.accountId1;
        this.accountId2 = operation.accountId2;
        this.time = operation.time;
        this.amount = operation.amount;
        this.fromType = operation.fromType;
        this.toType = operation.toType;
        this.comment = operation.comment;
    }

    @Override
    public Write13 newInstance() {
        return new Write13(this);
    }

    public long getAccountId1() {
        return accountId1;
    }

    public long getAccountId2() {
        return accountId2;
    }

    public Date getTime() {
        return time;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(ACCOUNT_ID1, accountId1)
            .put(ACCOUNT_ID2, accountId2)
            .put(TIME, time)
            .put(AMOUNT, amount)
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
        Write13 that = (Write13) o;
        return accountId1 == that.accountId1
            && accountId2 == that.accountId2
            && Objects.equals(time, that.time)
            && amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId1, accountId2, time.getTime(), amount);
    }

    @Override
    public String toString() {
        return "Write13{"
            + "accountId1="
            + accountId1
            + ", accountId2="
            + accountId2
            + ", time="
            + time
            + ", amount="
            + amount
            + '}';
    }
}

