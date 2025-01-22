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

public class ComplexRead9Result {
    public static final String RATIO_REPAY = "ratioRepay";
    public static final String RATIO_DEPOSIT = "ratioDeposit";
    public static final String RATIO_TRANSFER = "ratioTransfer";
    private final float ratioRepay;
    private final float ratioDeposit;
    private final float ratioTransfer;

    public ComplexRead9Result(@JsonProperty(RATIO_REPAY) float ratioRepay,
                              @JsonProperty(RATIO_DEPOSIT) float ratioDeposit,
                              @JsonProperty(RATIO_TRANSFER) float ratioTransfer) {
        this.ratioRepay = ratioRepay;
        this.ratioDeposit = ratioDeposit;
        this.ratioTransfer = ratioTransfer;
    }

    public float getRatioRepay() {
        return ratioRepay;
    }

    public float getRatioDeposit() {
        return ratioDeposit;
    }

    public float getRatioTransfer() {
        return ratioTransfer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead9Result that = (ComplexRead9Result) o;
        return ratioRepay == that.ratioRepay
            && ratioDeposit == that.ratioDeposit
            && ratioTransfer == that.ratioTransfer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ratioRepay, ratioDeposit, ratioTransfer);
    }

    @Override
    public String toString() {
        return "ComplexRead9Result{"
            + "ratioRepay="
            + ratioRepay
            + ", ratioDeposit="
            + ratioDeposit
            + ", ratioTransfer="
            + ratioTransfer
            + '}';
    }
}

