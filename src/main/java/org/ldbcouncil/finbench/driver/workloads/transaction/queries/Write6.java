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
 * Transaction workload write query 6:
 * -- Add Loan applied by Person --
 * Add a *Loan* node and add an *apply* edge from *Person* node to *Loan* node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write6 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1006;
    public static final String PERSON_ID = "personId";
    public static final String LOAN_ID = "loanId";
    public static final String LOAN_AMOUNT = "loanAmount";
    public static final String BALANCE = "balance";
    public static final String TIME = "time";
    public static final String LOAN_USAGE = "loanUsage";
    public static final String LOAN_RATE = "loanRate";
    public static final String LOAN_ORG = "loanOrg";
    public static final String COMMENT = "comment";
    private final long personId;
    private final long loanId;
    private final double loanAmount;
    private final double balance;
    private final Date time;
    private final String loanUsage;
    private final double loanRate;
    private final String loanOrg;
    private final String comment;

    public Write6(@JsonProperty(PERSON_ID) long personId,
                  @JsonProperty(LOAN_ID) long loanId,
                  @JsonProperty(LOAN_AMOUNT) double loanAmount,
                  @JsonProperty(BALANCE) double balance,
                  @JsonProperty(TIME) Date time,
                  @JsonProperty(LOAN_USAGE) String loanUsage,
                  @JsonProperty(LOAN_RATE) double loanRate,
                  @JsonProperty(LOAN_ORG) String loanOrg,
                  @JsonProperty(COMMENT) String comment) {
        this.personId = personId;
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.balance = balance;
        this.time = time;
        this.loanUsage = loanUsage;
        this.loanRate = loanRate;
        this.loanOrg = loanOrg;
        this.comment = comment;
    }

    public Write6(Write6 operation) {
        this.personId = operation.personId;
        this.loanId = operation.loanId;
        this.loanAmount = operation.loanAmount;
        this.balance = operation.balance;
        this.time = operation.time;
        this.loanUsage = operation.loanUsage;
        this.loanRate = operation.loanRate;
        this.loanOrg = operation.loanOrg;
        this.comment = operation.comment;
    }

    @Override
    public Write6 newInstance() {
        return new Write6(this);
    }

    public long getPersonId() {
        return personId;
    }

    public long getLoanId() {
        return loanId;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getBalance() {
        return balance;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(PERSON_ID, personId)
            .put(LOAN_ID, loanId)
            .put(LOAN_AMOUNT, loanAmount)
            .put(BALANCE, balance)
            .put(TIME, time)
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
        Write6 that = (Write6) o;
        return personId == that.personId
            && loanId == that.loanId
            && loanAmount == that.loanAmount
            && balance == that.balance
            && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, loanId, loanAmount, balance, time.getTime());
    }

    @Override
    public String toString() {
        return "Write6{"
            + "personId="
            + personId
            + ", loanId="
            + loanId
            + ", loanAmount="
            + loanAmount
            + ", balance="
            + balance
            + ", time="
            + time
            + '}';
    }
}

