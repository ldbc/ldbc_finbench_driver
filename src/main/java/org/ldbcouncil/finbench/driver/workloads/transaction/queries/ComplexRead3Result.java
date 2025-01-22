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

public class ComplexRead3Result {
    public static final String SHORTEST_PATH_LENGTH = "shortestPathLength";
    private final long shortestPathLength;

    public ComplexRead3Result(@JsonProperty(SHORTEST_PATH_LENGTH) long shortestPathLength) {
        this.shortestPathLength = shortestPathLength;
    }

    public long getShortestPathLength() {
        return shortestPathLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead3Result that = (ComplexRead3Result) o;
        return shortestPathLength == that.shortestPathLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortestPathLength);
    }

    @Override
    public String toString() {
        return "ComplexRead3Result{"
            + "shortestPathLength="
            + shortestPathLength
            + '}';
    }
}

