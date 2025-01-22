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

import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.ldbcouncil.finbench.driver.Operation;

public class OperationStreamBuffer implements Iterator<Iterator<Operation>> {

    private final BlockingQueue<Iterator<Operation>> blockingQueue;

    private boolean isEmpty = false;

    public OperationStreamBuffer(
        BlockingQueue<Iterator<Operation>> blockingQueue
    ) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public Iterator<Operation> next() {
        if (!isEmpty) {
            Iterator<Operation> opStream = null;
            try {
                opStream = blockingQueue.poll(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (opStream == null) {
                isEmpty = true;
                return Collections.emptyIterator();
            }
            return opStream;
        } else {
            return null;
        }
    }

    @Override
    public boolean hasNext() {
        return !blockingQueue.isEmpty();
    }
}
