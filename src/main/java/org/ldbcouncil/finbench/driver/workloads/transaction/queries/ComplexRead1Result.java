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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead1Result {
    public static final String OTHER_ID = "otherId";
    public static final String ACCOUNT_DISTANCE = "accountDistance";
    public static final String MEDIUM_ID = "mediumId";
    public static final String MEDIUM_TYPE = "mediumType";
    private final long otherId;
    private final int accountDistance;
    private final long mediumId;
    private final String mediumType;

    public ComplexRead1Result(@JsonProperty(OTHER_ID) long otherId,
                              @JsonProperty(ACCOUNT_DISTANCE) int accountDistance,
                              @JsonProperty(MEDIUM_ID) long mediumId,
                              @JsonProperty(MEDIUM_TYPE) String mediumType) {
        this.otherId = otherId;
        this.accountDistance = accountDistance;
        this.mediumId = mediumId;
        this.mediumType = mediumType;
    }

    public long getOtherId() {
        return otherId;
    }

    public int getAccountDistance() {
        return accountDistance;
    }

    public long getMediumId() {
        return mediumId;
    }

    public String getMediumType() {
        return mediumType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead1Result that = (ComplexRead1Result) o;
        return otherId == that.otherId
            && accountDistance == that.accountDistance
            && mediumId == that.mediumId
            && Objects.equals(mediumType, that.mediumType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(otherId, accountDistance, mediumId, mediumType);
    }

    @Override
    public String toString() {
        return "ComplexRead1Result{"
            + "otherId="
            + otherId
            + ", accountDistance="
            + accountDistance
            + ", mediumId="
            + mediumId
            + ", mediumType="
            + mediumType
            + '}';
    }
}

