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

public abstract class QueueEventFetcher<EVENT_TYPE> {
    public abstract EVENT_TYPE fetchNextEvent() throws InterruptedException;

    public static <TYPE> QueueEventFetcher<TYPE> queueEventFetcherFor(Queue<TYPE> queue) {
        return (BlockingQueue.class.isAssignableFrom(queue.getClass()))
            ? new BlockingQueueEventFetcher((BlockingQueue) queue)
            : new NonBlockingQueueEventFetcher(queue);
    }

    static class NonBlockingQueueEventFetcher<EVENT_TYPE_NON_BLOCKING>
        extends QueueEventFetcher<EVENT_TYPE_NON_BLOCKING> {
        private final Queue<EVENT_TYPE_NON_BLOCKING> queue;

        public NonBlockingQueueEventFetcher(Queue<EVENT_TYPE_NON_BLOCKING> queue) {
            this.queue = queue;
        }

        @Override
        public EVENT_TYPE_NON_BLOCKING fetchNextEvent() throws InterruptedException {
            EVENT_TYPE_NON_BLOCKING event;
            while (null == (event = queue.poll())) {
                LockSupport.parkNanos(1);
            }
            return event;
        }
    }

    static class BlockingQueueEventFetcher<EVENT_TYPE_BLOCKING> extends QueueEventFetcher<EVENT_TYPE_BLOCKING> {
        private final BlockingQueue<EVENT_TYPE_BLOCKING> queue;

        public BlockingQueueEventFetcher(BlockingQueue<EVENT_TYPE_BLOCKING> queue) {
            this.queue = queue;
        }

        @Override
        public EVENT_TYPE_BLOCKING fetchNextEvent() throws InterruptedException {
            return queue.take();
        }
    }
}
