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
 * Transaction workload write query 16:
 * -- Account signed in with Medium --
 *  Add a *signIn* edge from medium to an *Account* node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write16 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1016;
    public static final String MEDIUM_ID = "mediumId";
    public static final String ACCOUNT_ID = "accountId";
    public static final String TIME = "time";
    public static final String LOCATION = "location";
    public static final String COMMENT = "comment";
    private final long mediumId;
    private final long accountId;
    private final Date time;
    private final String location;
    private final String comment;

    public Write16(@JsonProperty(MEDIUM_ID) long mediumId,
                   @JsonProperty(ACCOUNT_ID) long accountId,
                   @JsonProperty(TIME) Date time,
                   @JsonProperty(LOCATION) String location,
                   @JsonProperty(COMMENT) String comment) {
        this.mediumId = mediumId;
        this.accountId = accountId;
        this.time = time;
        this.location = location;
        this.comment = comment;
    }

    public Write16(Write16 operation) {
        this.mediumId = operation.mediumId;
        this.accountId = operation.accountId;
        this.time = operation.time;
        this.location = operation.location;
        this.comment = operation.comment;
    }

    @Override
    public Write16 newInstance() {
        return new Write16(this);
    }

    public long getMediumId() {
        return mediumId;
    }

    public long getAccountId() {
        return accountId;
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
            .put(MEDIUM_ID, mediumId)
            .put(ACCOUNT_ID, accountId)
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
        Write16 that = (Write16) o;
        return mediumId == that.mediumId
            && accountId == that.accountId
            && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mediumId, accountId, time.getTime());
    }

    @Override
    public String toString() {
        return "Write16{"
            + "mediumId="
            + mediumId
            + ", accountId="
            + accountId
            + ", time="
            + time
            + '}';
    }
}

