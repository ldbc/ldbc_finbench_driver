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
import org.ldbcouncil.finbench.driver.util.Function0;

public class InterleaveGenerator<GENERATE_TYPE> extends Generator<GENERATE_TYPE> {
    private final Iterator<? extends GENERATE_TYPE> baseGenerator;
    private final Iterator<? extends GENERATE_TYPE> interleaveFromGenerator;
    private final Function0<Integer, RuntimeException> amountToInterleaveFun;

    private int remainingToInterleave;

    InterleaveGenerator(Iterator<? extends GENERATE_TYPE> baseGenerator,
                        Iterator<? extends GENERATE_TYPE> interleaveFromGenerator,
                        Function0<Integer, RuntimeException> amountToInterleaveFun) {
        this.baseGenerator = baseGenerator;
        this.interleaveFromGenerator = interleaveFromGenerator;
        this.amountToInterleaveFun = amountToInterleaveFun;
        this.remainingToInterleave = 0;
    }

    @Override
    protected GENERATE_TYPE doNext() throws GeneratorException {
        if (0 == remainingToInterleave) {
            if (baseGenerator.hasNext()) {
                remainingToInterleave = amountToInterleaveFun.apply();
                return baseGenerator.next();
            } else {
                return null;
            }
        } else {
            if (interleaveFromGenerator.hasNext()) {
                remainingToInterleave--;
                return interleaveFromGenerator.next();
            } else {
                return null;
            }
        }
    }
}
