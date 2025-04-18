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
 * Transaction workload write query 8:
 * -- Add Invest Between Person And Company --
 * Add a *invest* edge from a *Person* node to a *Company* node. 
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write8 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1008;
    public static final String PERSON_ID = "personId";
    public static final String COMPANY_ID = "companyId";
    public static final String TIME = "time";
    public static final String RATIO = "ratio";
    public static final String COMMENT = "comment";
    private final long personId;
    private final long companyId;
    private final Date time;
    private final double ratio;
    private final String comment;

    public Write8(@JsonProperty(PERSON_ID) long personId,
                  @JsonProperty(COMPANY_ID) long companyId,
                  @JsonProperty(TIME) Date time,
                  @JsonProperty(RATIO) double ratio,
                  @JsonProperty(COMMENT) String comment) {
        this.personId = personId;
        this.companyId = companyId;
        this.time = time;
        this.ratio = ratio;
        this.comment = comment;
    }

    public Write8(Write8 operation) {
        this.personId = operation.personId;
        this.companyId = operation.companyId;
        this.time = operation.time;
        this.ratio = operation.ratio;
        this.comment = operation.comment;
    }

    @Override
    public Write8 newInstance() {
        return new Write8(this);
    }

    public long getPersonId() {
        return personId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public Date getTime() {
        return time;
    }

    public double getRatio() {
        return ratio;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(PERSON_ID, personId)
            .put(COMPANY_ID, companyId)
            .put(TIME, time)
            .put(RATIO, ratio)
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
        Write8 that = (Write8) o;
        return personId == that.personId
            && companyId == that.companyId
            && Objects.equals(time, that.time)
            && ratio == that.ratio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, companyId, time.getTime(), ratio);
    }

    @Override
    public String toString() {
        return "Write8{"
            + "personId="
            + personId
            + ", companyId="
            + companyId
            + ", time="
            + time
            + ", ratio="
            + ratio
            + '}';
    }
}

