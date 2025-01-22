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
import org.ldbcouncil.finbench.driver.util.Function2;

// TODO test
public class MergingGenerator<FROM_GENERATE_TYPE_1_T, FROM_GENERATE_TYPE_2_T, TO_GENERATE_TYPE_T>
    extends Generator<TO_GENERATE_TYPE_T> {
    private final Iterator<FROM_GENERATE_TYPE_1_T> original1;
    private final Iterator<FROM_GENERATE_TYPE_2_T> original2;
    private final Function2<FROM_GENERATE_TYPE_1_T, FROM_GENERATE_TYPE_2_T, TO_GENERATE_TYPE_T, RuntimeException>
        mergeFun;

    MergingGenerator(
        Iterator<FROM_GENERATE_TYPE_1_T> original1,
        Iterator<FROM_GENERATE_TYPE_2_T> original2,
        Function2<FROM_GENERATE_TYPE_1_T, FROM_GENERATE_TYPE_2_T, TO_GENERATE_TYPE_T, RuntimeException> mergeFun) {
        this.original1 = original1;
        this.original2 = original2;
        this.mergeFun = mergeFun;
    }

    @Override
    protected TO_GENERATE_TYPE_T doNext() throws GeneratorException {
        if (original1.hasNext() && original2.hasNext()) {
            return mergeFun.apply(original1.next(), original2.next());
        } else {
            return null;
        }
    }
}
