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
import org.ldbcouncil.finbench.driver.Operation;

public class NothingOperation extends Operation<DummyResult> {
    public static final int TYPE = 0;

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.of();
    }

    @Override
    public boolean equals(Object that) {
        return true;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public DummyResult deserializeResult(String serializedOperationResult) {
        return new DummyResult();
    }
}
