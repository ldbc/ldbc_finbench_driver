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

public class SimpleRead6Result {
    public static final String DST_ID = "dstId";
    private final long dstId;

    public SimpleRead6Result(@JsonProperty(DST_ID) long dstId) {
        this.dstId = dstId;
    }

    public long getDstId() {
        return dstId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead6Result that = (SimpleRead6Result) o;
        return dstId == that.dstId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dstId);
    }

    @Override
    public String toString() {
        return "SimpleRead6Result{"
            + "dstId="
            + dstId
            + '}';
    }
}

