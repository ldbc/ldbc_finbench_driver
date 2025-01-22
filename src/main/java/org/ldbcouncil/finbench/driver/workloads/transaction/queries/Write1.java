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
 * Transaction workload write query 1:
 * -- Add a Person Node --
 * Add a Person node.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write1 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1001;
    public static final String PERSON_ID = "personId";
    public static final String PERSON_NAME = "personName";
    public static final String IS_BLOCKED = "isBlocked";
    public static final String Gender = "gender";
    public static final String Birthday = "birthday";
    public static final String Country = "country";
    public static final String City = "city";
    private final long personId;
    private final String personName;
    private final boolean isBlocked;
    private final String gender;
    private final String birthday;
    private final String country;
    private final String city;

    public Write1(@JsonProperty(PERSON_ID) long personId,
                  @JsonProperty(PERSON_NAME) String personName,
                  @JsonProperty(IS_BLOCKED) boolean isBlocked,
                  @JsonProperty(Gender) String gender,
                  @JsonProperty(Birthday) String birthday,
                  @JsonProperty(Country) String country,
                  @JsonProperty(City) String city) {
        this.personId = personId;
        this.personName = personName;
        this.isBlocked = isBlocked;
        this.gender = gender;
        this.birthday = birthday;
        this.country = country;
        this.city = city;
    }

    public Write1(Write1 operation) {
        this.personId = operation.personId;
        this.personName = operation.personName;
        this.isBlocked = operation.isBlocked;
        this.gender = operation.gender;
        this.birthday = operation.birthday;
        this.country = operation.country;
        this.city = operation.city;
    }

    @Override
    public Write1 newInstance() {
        return new Write1(this);
    }

    public long getPersonId() {
        return personId;
    }

    public String getPersonName() {
        return personName;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(PERSON_ID, personId)
            .put(PERSON_NAME, personName)
            .put(IS_BLOCKED, isBlocked)
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
        Write1 that = (Write1) o;
        return personId == that.personId
            && Objects.equals(personName, that.personName)
            && isBlocked == that.isBlocked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, personName, isBlocked);
    }

    @Override
    public String toString() {
        return "Write1{"
            + "personId="
            + personId
            + ", personName="
            + personName
            + ", isBlocked="
            + isBlocked
            + '}';
    }
}

