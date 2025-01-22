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

public class ComplexRead7Result {
    public static final String NUM_SRC = "numSrc";
    public static final String NUM_DST = "numDst";
    public static final String IN_OUT_RATIO = "inOutRatio";
    private final int numSrc;
    private final int numDst;
    private final float inOutRatio;

    public ComplexRead7Result(@JsonProperty(NUM_SRC) int numSrc,
                              @JsonProperty(NUM_DST) int numDst,
                              @JsonProperty(IN_OUT_RATIO) float inOutRatio) {
        this.numSrc = numSrc;
        this.numDst = numDst;
        this.inOutRatio = inOutRatio;
    }

    public int getNumSrc() {
        return numSrc;
    }

    public int getNumDst() {
        return numDst;
    }

    public float getInOutRatio() {
        return inOutRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead7Result that = (ComplexRead7Result) o;
        return numSrc == that.numSrc
            && numDst == that.numDst
            && inOutRatio == that.inOutRatio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numSrc, numDst, inOutRatio);
    }

    @Override
    public String toString() {
        return "ComplexRead7Result{"
            + "numSrc="
            + numSrc
            + ", numDst="
            + numDst
            + ", inOutRatio="
            + inOutRatio
            + '}';
    }
}

