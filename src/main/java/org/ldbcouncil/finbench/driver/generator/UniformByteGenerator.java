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

import org.apache.commons.math3.random.RandomDataGenerator;

public class UniformByteGenerator extends Generator<Byte> {
    private final RandomDataGenerator randomDataGenerator;
    private final byte[] buffer;
    private int currentIndexInBuffer;

    UniformByteGenerator(RandomDataGenerator randomDataGenerator) {
        this.randomDataGenerator = randomDataGenerator;
        this.buffer = new byte[8];
        this.currentIndexInBuffer = this.buffer.length;
        fillBufferIfEmpty();
    }

    private void fillBufferIfEmpty() {
        if (currentIndexInBuffer == buffer.length) {
            doFillBufferWithBytesFromRandomLong(buffer);
            currentIndexInBuffer = 0;
        }
    }

    private void doFillBufferWithBytesFromRandomLong(byte[] buffer) {
        long randomBytes = randomDataGenerator.nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
        buffer[0] = (byte) ((randomBytes >> 0) & 255);
        buffer[1] = (byte) ((randomBytes >> 8) & 255);
        buffer[2] = (byte) ((randomBytes >> 16) & 255);
        buffer[3] = (byte) ((randomBytes >> 24) & 255);
        buffer[4] = (byte) ((randomBytes >> 32) & 255);
        buffer[5] = (byte) ((randomBytes >> 40) & 255);
        buffer[6] = (byte) ((randomBytes >> 48) & 255);
        buffer[7] = (byte) ((randomBytes >> 56) & 255);
    }

    @Override
    protected Byte doNext() throws GeneratorException {
        fillBufferIfEmpty();
        return buffer[currentIndexInBuffer++];
    }
}
