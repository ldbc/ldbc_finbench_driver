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
import org.ldbcouncil.finbench.driver.util.NumberHelper;

public class IncrementingGenerator<GENERATE_TYPE extends Number> extends Generator<GENERATE_TYPE> {
    private final NumberHelper<GENERATE_TYPE> number;
    private final GENERATE_TYPE max;
    private final Iterator<GENERATE_TYPE> incrementByGenerator;
    private GENERATE_TYPE count;

    IncrementingGenerator(GENERATE_TYPE start, Iterator<GENERATE_TYPE> incrementByGenerator, GENERATE_TYPE max) {
        this.count = start;
        this.incrementByGenerator = incrementByGenerator;
        this.max = max;
        this.number = NumberHelper.createNumberHelper(start.getClass());
    }

    @Override
    protected GENERATE_TYPE doNext() {
        if (null != max && number.gt(count, max)) {
            return null;
        }
        GENERATE_TYPE next = count;
        count = number.sum(count, incrementByGenerator.next());
        return next;
    }
}
