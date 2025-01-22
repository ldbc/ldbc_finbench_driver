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

public class SizedUniformByteGeneratorGenerator extends Generator<Iterator<Byte>> {
    private final Iterator<Long> lengths;
    private final GeneratorFactory gf;

    SizedUniformByteGeneratorGenerator(Iterator<Long> lengths, GeneratorFactory gf) {
        this.lengths = lengths;
        this.gf = gf;
    }

    @Override
    protected Iterator<Byte> doNext() throws GeneratorException {
        return gf.limit(gf.uniformBytes(), lengths.next());
    }

}
