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

import com.google.common.collect.Lists;
import java.util.Iterator;

public class RepeatingGenerator<GENERATE_TYPE> extends Generator<GENERATE_TYPE> {
    private final Iterable<GENERATE_TYPE> generatorIterable;
    private Iterator<GENERATE_TYPE> generator;

    RepeatingGenerator(Iterator<GENERATE_TYPE> generator) {
        this.generatorIterable = Lists.newArrayList(generator);
        this.generator = this.generatorIterable.iterator();
    }

    @Override
    protected GENERATE_TYPE doNext() throws GeneratorException {
        if (generator.hasNext()) {
            return generator.next();
        } else {
            generator = generatorIterable.iterator();
            return (generator.hasNext()) ? generator.next() : null;
        }
    }
}
