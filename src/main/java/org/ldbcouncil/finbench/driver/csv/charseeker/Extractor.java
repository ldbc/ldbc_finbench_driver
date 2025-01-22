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

package org.ldbcouncil.finbench.driver.csv.charseeker;

/**
 * Extracts a value from a part of a {@code char[]} into any type of value, f.ex. a {@link Extractors#string()}
 * string},
 * {@link Extractors#long_()}  long} or {@link Extractors#intArray()}.
 * <p/>
 * An {@link Extractor} is mutable for the single purpose of ability to reuse its value instance. Consider extracting
 * a primitive int -
 * <p/>
 * Sub-interfaces and implementations can and should specify specific accessors for the purpose
 * of performance and less garbage, f.ex. where an IntExtractor could have an accessor method for
 * getting the extracted value as primitive int, to avoid auto-boxing which would arise from calling {@link #value()}.
 *
 * @see Extractors for a collection of very common extractors.
 */
public interface Extractor<T> {
    void extract(char[] data, int offset, int length);

    T value();

    /**
     * @return string representation of what type of value of produces. Also used as key in {@link Extractors}.
     */
    @Override
    String toString();
}
