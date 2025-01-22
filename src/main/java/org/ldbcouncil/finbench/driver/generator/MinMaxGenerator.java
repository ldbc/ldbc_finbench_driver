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


public class MinMaxGenerator<GENERATE_TYPE extends Number> extends Generator<GENERATE_TYPE> {
    private GENERATE_TYPE min = null;
    private GENERATE_TYPE max = null;
    private final Iterator<GENERATE_TYPE> generator;

    MinMaxGenerator(Iterator<GENERATE_TYPE> generator, GENERATE_TYPE initialMin, GENERATE_TYPE initialMax) {
        this.min = initialMin;
        this.max = initialMax;
        this.generator = generator;
    }

    @Override
    protected GENERATE_TYPE doNext() throws GeneratorException {
        if (!generator.hasNext()) {
            return null;
        }
        GENERATE_TYPE next = generator.next();
        min = (next.doubleValue() < min.doubleValue()) ? next : min;
        max = (next.doubleValue() > max.doubleValue()) ? next : max;
        return next;

    }

    public final GENERATE_TYPE getMin() {
        return min;
    }

    public final GENERATE_TYPE getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "MinMaxGeneratorWrapper [min=" + min + ", max=" + max + ", generator=" + generator + "]";
    }
}
