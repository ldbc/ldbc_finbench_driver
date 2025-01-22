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

package org.ldbcouncil.finbench.driver.generator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class Generator<GENERATE_TYPE> implements Iterator<GENERATE_TYPE> {
    private GENERATE_TYPE next = null;

    // Return null if nothing more to generate
    protected abstract GENERATE_TYPE doNext() throws GeneratorException;

    public final GENERATE_TYPE next() {
        next = (next == null) ? doNext() : next;
        if (null == next) {
            throw new NoSuchElementException("Generator has nothing more to generate");
        }
        GENERATE_TYPE tempNext = next;
        next = null;
        return tempNext;
    }

    @Override
    public final boolean hasNext() {
        next = (next == null) ? doNext() : next;
        return (next != null);
    }

    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
