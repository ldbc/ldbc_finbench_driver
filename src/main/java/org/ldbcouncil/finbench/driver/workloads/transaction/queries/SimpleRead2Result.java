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

public class SimpleRead2Result {
    public static final String SUM_EDGE1_AMOUNT = "sumEdge1Amount";
    public static final String MAX_EDGE1_AMOUNT = "maxEdge1Amount";
    public static final String NUM_EDGE1 = "numEdge1";
    public static final String SUM_EDGE2_AMOUNT = "sumEdge2Amount";
    public static final String MAX_EDGE2_AMOUNT = "maxEdge2Amount";
    public static final String NUM_EDGE2 = "numEdge2";
    private final double sumEdge1Amount;
    private final double maxEdge1Amount;
    private final long numEdge1;
    private final double sumEdge2Amount;
    private final double maxEdge2Amount;
    private final long numEdge2;

    public SimpleRead2Result(@JsonProperty(SUM_EDGE1_AMOUNT) double sumEdge1Amount,
                             @JsonProperty(MAX_EDGE1_AMOUNT) double maxEdge1Amount,
                             @JsonProperty(NUM_EDGE1) long numEdge1,
                             @JsonProperty(SUM_EDGE2_AMOUNT) double sumEdge2Amount,
                             @JsonProperty(MAX_EDGE2_AMOUNT) double maxEdge2Amount,
                             @JsonProperty(NUM_EDGE2) long numEdge2) {
        this.sumEdge1Amount = sumEdge1Amount;
        this.maxEdge1Amount = maxEdge1Amount;
        this.numEdge1 = numEdge1;
        this.sumEdge2Amount = sumEdge2Amount;
        this.maxEdge2Amount = maxEdge2Amount;
        this.numEdge2 = numEdge2;
    }

    public double getSumEdge1Amount() {
        return sumEdge1Amount;
    }

    public double getMaxEdge1Amount() {
        return maxEdge1Amount;
    }

    public long getNumEdge1() {
        return numEdge1;
    }

    public double getSumEdge2Amount() {
        return sumEdge2Amount;
    }

    public double getMaxEdge2Amount() {
        return maxEdge2Amount;
    }

    public long getNumEdge2() {
        return numEdge2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead2Result that = (SimpleRead2Result) o;
        return sumEdge1Amount == that.sumEdge1Amount
            && maxEdge1Amount == that.maxEdge1Amount
            && numEdge1 == that.numEdge1
            && sumEdge2Amount == that.sumEdge2Amount
            && maxEdge2Amount == that.maxEdge2Amount
            && numEdge2 == that.numEdge2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sumEdge1Amount, maxEdge1Amount, numEdge1, sumEdge2Amount, maxEdge2Amount, numEdge2);
    }

    @Override
    public String toString() {
        return "SimpleRead2Result{"
            + "sumEdge1Amount="
            + sumEdge1Amount
            + ", maxEdge1Amount="
            + maxEdge1Amount
            + ", numEdge1="
            + numEdge1
            + ", sumEdge2Amount="
            + sumEdge2Amount
            + ", maxEdge2Amount="
            + maxEdge2Amount
            + ", numEdge2="
            + numEdge2
            + '}';
    }
}

