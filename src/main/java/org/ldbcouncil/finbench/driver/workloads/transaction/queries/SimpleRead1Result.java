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
import java.util.Date;
import java.util.Objects;

public class SimpleRead1Result {
    public static final String CREATE_TIME = "createTime";
    public static final String IS_BLOCKED = "isBlocked";
    public static final String TYPE = "type";
    private final Date createTime;
    private final boolean isBlocked;
    private final String type;

    public SimpleRead1Result(@JsonProperty(CREATE_TIME) Date createTime,
                             @JsonProperty(IS_BLOCKED) boolean isBlocked,
                             @JsonProperty(TYPE) String type) {
        this.createTime = createTime;
        this.isBlocked = isBlocked;
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead1Result that = (SimpleRead1Result) o;
        return Objects.equals(createTime, that.createTime)
            && isBlocked == that.isBlocked
            && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createTime, isBlocked, type);
    }

    @Override
    public String toString() {
        return "SimpleRead1Result{"
            + "createTime="
            + createTime
            + ", isBlocked="
            + isBlocked
            + ", type="
            + type
            + '}';
    }
}

