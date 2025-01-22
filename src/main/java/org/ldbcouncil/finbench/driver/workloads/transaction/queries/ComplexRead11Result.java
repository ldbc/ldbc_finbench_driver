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

public class ComplexRead11Result {
    public static final String SUM_LOAN_AMOUNT = "sumLoanAmount";
    public static final String NUM_LOANS = "numLoans";
    private final double sumLoanAmount;
    private final int numLoans;

    public ComplexRead11Result(@JsonProperty(SUM_LOAN_AMOUNT) double sumLoanAmount,
                               @JsonProperty(NUM_LOANS) int numLoans) {
        this.sumLoanAmount = sumLoanAmount;
        this.numLoans = numLoans;
    }

    public double getSumLoanAmount() {
        return sumLoanAmount;
    }

    public int getNumLoans() {
        return numLoans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead11Result that = (ComplexRead11Result) o;
        return sumLoanAmount == that.sumLoanAmount
            && numLoans == that.numLoans;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sumLoanAmount, numLoans);
    }

    @Override
    public String toString() {
        return "ComplexRead11Result{"
            + "sumLoanAmount="
            + sumLoanAmount
            + ", numLoans="
            + numLoans
            + '}';
    }
}

