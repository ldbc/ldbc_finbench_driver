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
 * Transaction workload write query 14:
 * -- Add Repay Between Account And Loan --
 * AAdd a *repay* edge from an *Account* node to a *Loan* node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write14 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1014;
    public static final String ACCOUNT_ID = "accountId";
    public static final String LOAN_ID = "loanId";
    public static final String TIME = "time";
    public static final String AMOUNT = "amount";
    public static final String COMMENT = "comment";
    private final long accountId;
    private final long loanId;
    private final Date time;
    private final double amount;
    private final String comment;

    public Write14(@JsonProperty(ACCOUNT_ID) long accountId,
                   @JsonProperty(LOAN_ID) long loanId,
                   @JsonProperty(TIME) Date time,
                   @JsonProperty(AMOUNT) double amount,
                   @JsonProperty(COMMENT) String comment) {
        this.accountId = accountId;
        this.loanId = loanId;
        this.time = time;
        this.amount = amount;
        this.comment = comment;
    }

    public Write14(Write14 operation) {
        this.accountId = operation.accountId;
        this.loanId = operation.loanId;
        this.time = operation.time;
        this.amount = operation.amount;
        this.comment = operation.comment;
    }

    @Override
    public Write14 newInstance() {
        return new Write14(this);
    }

    public long getAccountId() {
        return accountId;
    }

    public long getLoanId() {
        return loanId;
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
            .put(ACCOUNT_ID, accountId)
            .put(LOAN_ID, loanId)
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
        Write14 that = (Write14) o;
        return accountId == that.accountId
            && loanId == that.loanId
            && Objects.equals(time, that.time)
            && amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, loanId, time.getTime(), amount);
    }

    @Override
    public String toString() {
        return "Write14{"
            + "accountId="
            + accountId
            + ", loanId="
            + loanId
            + ", time="
            + time
            + ", amount="
            + amount
            + '}';
    }
}

