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
import java.util.concurrent.locks.LockSupport;

public abstract class QueueEventSubmitter<EVENT_TYPE> {

    public static <TYPE> QueueEventSubmitter<TYPE> queueEventSubmitterFor(Queue<TYPE> queue) {
        return (BlockingQueue.class.isAssignableFrom(queue.getClass()))
            ? new BlockingQueueEventSubmitter((BlockingQueue) queue)
            : new NonBlockingQueueEventSubmitter(queue);
    }

    public abstract void submitEventToQueue(EVENT_TYPE event) throws InterruptedException;

    static class NonBlockingQueueEventSubmitter<EVENT_TYPE_NON_BLOCKING>
        extends QueueEventSubmitter<EVENT_TYPE_NON_BLOCKING> {
        private final Queue<EVENT_TYPE_NON_BLOCKING> queue;

        private NonBlockingQueueEventSubmitter(Queue<EVENT_TYPE_NON_BLOCKING> queue) {
            this.queue = queue;
        }

        @Override
        public void submitEventToQueue(EVENT_TYPE_NON_BLOCKING event) throws InterruptedException {
            while (!queue.offer(event)) {
                LockSupport.parkNanos(1);
            }
        }
    }

    static class BlockingQueueEventSubmitter<EVENT_TYPE_BLOCKING> extends QueueEventSubmitter<EVENT_TYPE_BLOCKING> {
        private final BlockingQueue<EVENT_TYPE_BLOCKING> queue;

        private BlockingQueueEventSubmitter(BlockingQueue<EVENT_TYPE_BLOCKING> queue) {
            this.queue = queue;
        }

        @Override
        public void submitEventToQueue(EVENT_TYPE_BLOCKING event) throws InterruptedException {
            queue.put(event);
        }
    }
}
