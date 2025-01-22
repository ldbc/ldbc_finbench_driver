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

package org.ldbcouncil.finbench.driver.workloads.dummy;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class TimedNamedOperation3 extends NothingOperation {
    public static final int TYPE = 3;
    public static final String NAME = "name";
    private final String name;

    public TimedNamedOperation3(long scheduledStartTimeAsMilli, long timeStamp, long dependencyTime, String name) {
        setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
        setTimeStamp(timeStamp);
        setDependencyTimeStamp(dependencyTime);
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(NAME, name)
            .build();
    }

    @Override
    public String toString() {
        return "TimedNamedOperation3{"
            + "scheduledStartTime="
            + scheduledStartTimeAsMilli()
            + ", timeStamp="
            + timeStamp()
            + ", dependencyTimeStamp="
            + dependencyTimeStamp()
            + ", name='"
            + name
            + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        TimedNamedOperation3 operation = (TimedNamedOperation3) o;

        if (timeStamp() != operation.timeStamp()) {
            return false;
        }
        if (dependencyTimeStamp() != operation.dependencyTimeStamp()) {
            return false;
        }
        if (scheduledStartTimeAsMilli() != operation.scheduledStartTimeAsMilli()) {
            return false;
        }
        return name != null ? name.equals(operation.name) : operation.name == null;
    }

    @Override
    public int type() {
        return TYPE;
    }
}
