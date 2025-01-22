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

import java.util.Collections;
import java.util.Iterator;
import org.ldbcouncil.finbench.driver.Operation;

public class BufferedIterator implements Iterator<Operation> {

    private final OperationStreamBuffer operationStreamBuffer;
    private Iterator<Operation> currentOperationStream = Collections.emptyIterator();
    private boolean isEmpty = false;

    public BufferedIterator(
        OperationStreamBuffer operationStreamBuffer
    ) {
        this.operationStreamBuffer = operationStreamBuffer;
    }

    public void init() {
        currentOperationStream = operationStreamBuffer.next();
    }

    @Override
    public boolean hasNext() {
        if (!currentOperationStream.hasNext() && !isEmpty) {
            currentOperationStream = operationStreamBuffer.next();
            if (currentOperationStream == null) {
                currentOperationStream = Collections.emptyIterator();
                isEmpty = true;
            }
            if (!currentOperationStream.hasNext()) {
                isEmpty = true;
            }
        }
        return currentOperationStream.hasNext();
    }

    @Override
    public Operation next() {
        if (hasNext()) {
            return currentOperationStream.next();
        } else {
            return null;
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException(format("%s does not support remove()", getClass().getSimpleName()));
    }
}
