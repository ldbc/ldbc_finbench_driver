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

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.ldbcouncil.finbench.driver.util.NumberHelper;

public class ExponentialNumberGenerator<GENERATE_TYPE extends Number> extends Generator<GENERATE_TYPE> {
    private final ExponentialDistribution exponentialDistribution;
    private final NumberHelper<GENERATE_TYPE> number;

    ExponentialNumberGenerator(RandomDataGenerator random, GENERATE_TYPE mean) {
        this.exponentialDistribution = new ExponentialDistribution(random.getRandomGenerator(), mean.doubleValue(),
            ExponentialDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
        this.number = NumberHelper.createNumberHelper(mean.getClass());
    }

    @Override
    protected GENERATE_TYPE doNext() throws GeneratorException {
        return number.cast(exponentialDistribution.sample());
    }
}
