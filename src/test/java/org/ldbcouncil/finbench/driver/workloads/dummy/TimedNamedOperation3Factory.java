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

import java.util.Iterator;
import org.ldbcouncil.finbench.driver.Operation;

public class TimedNamedOperation3Factory implements Iterator<Operation> {
    private final Iterator<Long> startTimes;
    private final Iterator<Long> dependencyTimes;
    private final Iterator<String> names;

    public TimedNamedOperation3Factory(Iterator<Long> startTimes,
                                       Iterator<Long> dependencyTimes,
                                       Iterator<String> names) {
        this.startTimes = startTimes;
        this.dependencyTimes = dependencyTimes;
        this.names = names;
    }

    @Override
    public boolean hasNext() {
        return startTimes.hasNext() & dependencyTimes.hasNext();
    }

    @Override
    public TimedNamedOperation3 next() {
        long startTime = startTimes.next();
        return new TimedNamedOperation3(startTime, startTime, dependencyTimes.next(), names.next());
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
