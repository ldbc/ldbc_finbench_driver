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

package org.ldbcouncil.finbench.driver.testutils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.ldbcouncil.finbench.driver.runtime.scheduling.Spinner;

public class ThreadPoolLoadGenerator {
    private final int threadCount;
    private final ExecutorService executorService;
    private final long sleepDurationAsMilli;
    private final AtomicBoolean sharedDoTerminateReference = new AtomicBoolean(false);
    private final AtomicBoolean hasStarted = new AtomicBoolean(false);

    ThreadPoolLoadGenerator(int threadCount, long sleepDurationAsMilli) {
        this.threadCount = threadCount;
        this.sleepDurationAsMilli = sleepDurationAsMilli;
        ThreadFactory threadFactory = new ThreadFactory() {
            private final long factoryTimeStampId = System.currentTimeMillis();
            int count = 0;

            @Override
            public Thread newThread(Runnable runnable) {
                Thread newThread = new Thread(
                    runnable,
                    ThreadPoolLoadGenerator.class.getSimpleName()
                        + "-id("
                        + factoryTimeStampId
                        + ")"
                        + "-thread("
                        + count++
                        + ")");
                return newThread;
            }
        };

        this.executorService = Executors.newFixedThreadPool(threadCount, threadFactory);
    }

    public synchronized void start() {
        if (hasStarted.get()) {
            return;
        }

        for (int i = 0; i < threadCount; i++) {
            LoadGeneratingTask task = new LoadGeneratingTask(sharedDoTerminateReference, sleepDurationAsMilli);
            executorService.execute(task);
        }
        hasStarted.set(true);
    }

    public synchronized boolean shutdown(long shutdownTimeoutAsMilli) throws InterruptedException {
        if (true == sharedDoTerminateReference.get()) {
            return true;
        }

        sharedDoTerminateReference.set(true);

        executorService.shutdown();
        boolean allHandlersCompleted = executorService.awaitTermination(shutdownTimeoutAsMilli, TimeUnit.MILLISECONDS);
        return allHandlersCompleted;
    }

    private static class LoadGeneratingTask implements Runnable {
        private final AtomicBoolean sharedDoTerminateReference;
        private final long sleepDurationAsMilli;

        private LoadGeneratingTask(AtomicBoolean sharedDoTerminateReference, long sleepDurationAsMilli) {
            this.sharedDoTerminateReference = sharedDoTerminateReference;
            this.sleepDurationAsMilli = sleepDurationAsMilli;
        }

        @Override
        public void run() {
            while (false == sharedDoTerminateReference.get()) {
                Spinner.powerNap(sleepDurationAsMilli);
            }
        }
    }
}
