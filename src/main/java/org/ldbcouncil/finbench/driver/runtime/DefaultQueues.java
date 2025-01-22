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

package org.ldbcouncil.finbench.driver.runtime;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class DefaultQueues {
    public static <T> Queue<T> newNonBlocking() {
        return new ConcurrentLinkedQueue<>();
    }

    public static <T> BlockingQueue<T> newBlockingUnbounded() {
        return new LinkedTransferQueue<>();
    }

    public static final int DEFAULT_BOUND_1000 = 1000;

    public static <T> BlockingQueue<T> newBlockingBounded(int capacity) {
        return new LinkedBlockingQueue<>(capacity);
    }

    public static <T> BlockingQueue<T> newAlwaysBlockingBounded(int capacity) {
        return new AlwaysBlockingLinkedBlockingQueue<>(capacity);
    }

    /*
    turn offer() & add() into blocking calls (unless interrupted)
    */
    private static class AlwaysBlockingLinkedBlockingQueue<E> extends LinkedBlockingQueue<E> {
        public AlwaysBlockingLinkedBlockingQueue(int maxSize) {
            super(maxSize);
        }

        @Override
        public boolean offer(E e) {
            try {
                put(e);
                return true;
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            return false;
        }

        @Override
        public boolean add(E e) {
            try {
                put(e);
                return true;
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            return false;
        }
    }
}
