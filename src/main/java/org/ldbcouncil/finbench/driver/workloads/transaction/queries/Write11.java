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
 * Transaction workload write query 11:
 * -- Add Guarantee Between Companies --
 * Add a *guarantee* edge from a *Company* node to another *Company* node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write11 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1011;
    public static final String COMPANY_ID1 = "companyId1";
    public static final String COMPANY_ID2 = "companyId2";
    public static final String TIME = "time";
    public static final String RELATION = "relation";
    public static final String COMMENT = "comment";
    private final long companyId1;
    private final long companyId2;
    private final Date time;
    private final String relation;
    private final String comment;

    public Write11(@JsonProperty(COMPANY_ID1) long companyId1,
                   @JsonProperty(COMPANY_ID2) long companyId2,
                   @JsonProperty(TIME) Date time,
                   @JsonProperty(RELATION) String relation,
                   @JsonProperty(COMMENT) String comment) {
        this.companyId1 = companyId1;
        this.companyId2 = companyId2;
        this.time = time;
        this.relation = relation;
        this.comment = comment;
    }

    public Write11(Write11 operation) {
        this.companyId1 = operation.companyId1;
        this.companyId2 = operation.companyId2;
        this.time = operation.time;
        this.relation = operation.relation;
        this.comment = operation.comment;
    }

    @Override
    public Write11 newInstance() {
        return new Write11(this);
    }

    public long getCompanyId1() {
        return companyId1;
    }

    public long getCompanyId2() {
        return companyId2;
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
            .put(COMPANY_ID1, companyId1)
            .put(COMPANY_ID2, companyId2)
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
        Write11 that = (Write11) o;
        return companyId1 == that.companyId1
            && companyId2 == that.companyId2
            && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId1, companyId2, time.getTime());
    }

    @Override
    public String toString() {
        return "Write11{"
            + "companyId1="
            + companyId1
            + ", companyId2="
            + companyId2
            + ", time="
            + time
            + '}';
    }
}

