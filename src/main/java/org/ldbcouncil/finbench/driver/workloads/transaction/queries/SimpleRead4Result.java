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

public class SimpleRead4Result {
    public static final String DST_ID = "dstId";
    public static final String NUM_EDGES = "numEdges";
    public static final String SUM_AMOUNT = "sumAmount";
    private final long dstId;
    private final int numEdges;
    private final double sumAmount;

    public SimpleRead4Result(@JsonProperty(DST_ID) long dstId,
                             @JsonProperty(NUM_EDGES) int numEdges,
                             @JsonProperty(SUM_AMOUNT) double sumAmount) {
        this.dstId = dstId;
        this.numEdges = numEdges;
        this.sumAmount = sumAmount;
    }

    public long getDstId() {
        return dstId;
    }

    public int getNumEdges() {
        return numEdges;
    }

    public double getSumAmount() {
        return sumAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead4Result that = (SimpleRead4Result) o;
        return dstId == that.dstId
            && numEdges == that.numEdges
            && sumAmount == that.sumAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dstId, numEdges, sumAmount);
    }

    @Override
    public String toString() {
        return "SimpleRead4Result{"
            + "dstId="
            + dstId
            + ", numEdges="
            + numEdges
            + ", sumAmount="
            + sumAmount
            + '}';
    }
}

