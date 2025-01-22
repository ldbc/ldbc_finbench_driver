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

public class SimpleRead3Result {
    public static final String BLOCK_RATIO = "blockRatio";
    private final float blockRatio;

    public SimpleRead3Result(@JsonProperty(BLOCK_RATIO) float blockRatio) {
        this.blockRatio = blockRatio;
    }

    public float getBlockRatio() {
        return blockRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead3Result that = (SimpleRead3Result) o;
        return blockRatio == that.blockRatio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockRatio);
    }

    @Override
    public String toString() {
        return "SimpleRead3Result{"
            + "blockRatio="
            + blockRatio
            + '}';
    }
}

