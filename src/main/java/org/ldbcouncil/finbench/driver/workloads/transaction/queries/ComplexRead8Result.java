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

public class ComplexRead8Result {
    public static final String DST_ID = "dstId";
    public static final String RATIO = "ratio";
    public static final String MIN_DISTANCE_FROM_LOAN = "minDistanceFromLoan";
    private final long dstId;
    private final float ratio;
    private final int minDistanceFromLoan;

    public ComplexRead8Result(@JsonProperty(DST_ID) long dstId,
                              @JsonProperty(RATIO) float ratio,
                              @JsonProperty(MIN_DISTANCE_FROM_LOAN) int minDistanceFromLoan) {
        this.dstId = dstId;
        this.ratio = ratio;
        this.minDistanceFromLoan = minDistanceFromLoan;
    }

    public long getDstId() {
        return dstId;
    }

    public float getRatio() {
        return ratio;
    }

    public int getMinDistanceFromLoan() {
        return minDistanceFromLoan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead8Result that = (ComplexRead8Result) o;
        return dstId == that.dstId
            && ratio == that.ratio
            && minDistanceFromLoan == that.minDistanceFromLoan;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dstId, ratio, minDistanceFromLoan);
    }

    @Override
    public String toString() {
        return "ComplexRead8Result{"
            + "dstId="
            + dstId
            + ", ratio="
            + ratio
            + ", minDistanceFromLoan="
            + minDistanceFromLoan
            + '}';
    }
}

