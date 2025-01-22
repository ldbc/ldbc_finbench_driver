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
import org.ldbcouncil.finbench.driver.util.Function1;

public class MappingGenerator<FROM_GENERATE_TYPE, TO_GENERATE_TYPE> extends Generator<TO_GENERATE_TYPE> {
    private final Iterator<FROM_GENERATE_TYPE> fromGenerator;
    private final Function1<FROM_GENERATE_TYPE, TO_GENERATE_TYPE, RuntimeException> mapFunction;

    public MappingGenerator(
        Iterator<FROM_GENERATE_TYPE> fromGenerator,
        Function1<FROM_GENERATE_TYPE, TO_GENERATE_TYPE, RuntimeException> mapFunction) {
        this.fromGenerator = fromGenerator;
        this.mapFunction = mapFunction;
    }

    @Override
    protected TO_GENERATE_TYPE doNext() throws GeneratorException {
        if (fromGenerator.hasNext()) {
            return mapFunction.apply(fromGenerator.next());
        } else {
            return null;
        }
    }
}
