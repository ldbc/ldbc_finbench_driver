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

public class TimedNamedOperation1Factory implements Iterator<Operation> {
    private final Iterator<Long> startTimesAsMilli;
    private final Iterator<Long> dependencyTimesAsMilli;
    private final Iterator<String> names;

    public TimedNamedOperation1Factory(Iterator<Long> startTimesAsMilli,
                                       Iterator<Long> dependencyTimesAsMilli,
                                       Iterator<String> names) {
        this.startTimesAsMilli = startTimesAsMilli;
        this.dependencyTimesAsMilli = dependencyTimesAsMilli;
        this.names = names;
    }

    @Override
    public boolean hasNext() {
        return startTimesAsMilli.hasNext() & dependencyTimesAsMilli.hasNext();
    }

    @Override
    public TimedNamedOperation1 next() {
        long startTime = startTimesAsMilli.next();
        return new TimedNamedOperation1(startTime, startTime, dependencyTimesAsMilli.next(), names.next());
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
