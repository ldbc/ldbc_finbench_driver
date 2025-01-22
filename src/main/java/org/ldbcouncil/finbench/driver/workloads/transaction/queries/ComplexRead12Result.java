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

public class ComplexRead12Result {
    public static final String COMP_ACCOUNT_ID = "compAccountId";
    public static final String SUM_EDGE2_AMOUNT = "sumEdge2Amount";
    private final long compAccountId;
    private final double sumEdge2Amount;

    public ComplexRead12Result(@JsonProperty(COMP_ACCOUNT_ID) long compAccountId,
                               @JsonProperty(SUM_EDGE2_AMOUNT) double sumEdge2Amount) {
        this.compAccountId = compAccountId;
        this.sumEdge2Amount = sumEdge2Amount;
    }

    public long getCompAccountId() {
        return compAccountId;
    }

    public double getSumEdge2Amount() {
        return sumEdge2Amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead12Result that = (ComplexRead12Result) o;
        return compAccountId == that.compAccountId
            && sumEdge2Amount == that.sumEdge2Amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(compAccountId, sumEdge2Amount);
    }

    @Override
    public String toString() {
        return "ComplexRead12Result{"
            + "compAccountId="
            + compAccountId
            + ", sumEdge2Amount="
            + sumEdge2Amount
            + '}';
    }
}

