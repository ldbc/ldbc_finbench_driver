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

import static java.lang.String.format;

import java.util.Iterator;
import org.ldbcouncil.finbench.driver.util.NumberHelper;

public class NaiveBoundedRangeNumberGenerator<GENERATE_TYPE extends Number> extends Generator<GENERATE_TYPE> {
    private final Integer maxIterations = 1000;
    private final MinMaxGenerator<GENERATE_TYPE> lowerBoundGenerator;
    private final MinMaxGenerator<GENERATE_TYPE> upperBoundGenerator;
    private final Iterator<GENERATE_TYPE> generator;
    private final NumberHelper<GENERATE_TYPE> number;

    NaiveBoundedRangeNumberGenerator(Iterator<GENERATE_TYPE> generator,
                                     MinMaxGenerator<GENERATE_TYPE> lowerBoundGenerator,
                                     MinMaxGenerator<GENERATE_TYPE> upperBoundGenerator) {
        this.lowerBoundGenerator = lowerBoundGenerator;
        this.upperBoundGenerator = upperBoundGenerator;
        this.generator = generator;
        this.number = NumberHelper.createNumberHelper(lowerBoundGenerator.getMin().getClass());
    }

    @Override
    protected GENERATE_TYPE doNext() throws GeneratorException {
        GENERATE_TYPE next;
        for (int i = 0; i < maxIterations; i++) {
            next = generator.next();
            if (number.gte(next, lowerBoundGenerator.getMin()) && number.lte(next, upperBoundGenerator.getMax())) {
                return next;
            }
        }
        throw new GeneratorException(
            format("Random in-range number not found within maxIterations[%s]", maxIterations)
        );
    }
}
