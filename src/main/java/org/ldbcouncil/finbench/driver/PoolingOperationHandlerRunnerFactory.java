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

package org.ldbcouncil.finbench.driver;

import static java.lang.String.format;

import java.util.concurrent.TimeUnit;
import org.ldbcouncil.finbench.driver.temporal.TemporalUtil;
import stormpot.Allocator;
import stormpot.BlazePool;
import stormpot.Completion;
import stormpot.Config;
import stormpot.Expiration;
import stormpot.Slot;
import stormpot.SlotInfo;
import stormpot.Timeout;

public class PoolingOperationHandlerRunnerFactory implements OperationHandlerRunnerFactory {
    private static final int INITIAL_POOL_SIZE = 512;
    private static final int MAX_POOL_SIZE = (int) Math.round(Math.pow(2, 15)); // ~32,000
    private static final Timeout POOL_CLAIM_TIMEOUT = new Timeout(100, TimeUnit.MILLISECONDS);
    private static final Timeout POOL_CLAIM_AFTER_RESIZE_TIMEOUT = new Timeout(1000, TimeUnit.MILLISECONDS);
    private static final Timeout POOL_SHUTDOWN_TIMEOUT = new Timeout(10, TimeUnit.SECONDS);
    private final BlazePool<OperationHandlerRunnableContext> operationHandlerRunnerPool;
    private final OperationHandlerRunnerFactory innerOperationHandlerRunnerFactory;
    int highestSetPoolSize = 0;

    public PoolingOperationHandlerRunnerFactory(OperationHandlerRunnerFactory operationHandlerRunnerFactory) {
        this.innerOperationHandlerRunnerFactory = operationHandlerRunnerFactory;
        OperationHandlerRunnerAllocator operationHandlerRunnerAllocator =
            new OperationHandlerRunnerAllocator(innerOperationHandlerRunnerFactory);
        Config<OperationHandlerRunnableContext> operationHandlerRunnerPoolConfig = new Config<>();
        operationHandlerRunnerPoolConfig.setAllocator(operationHandlerRunnerAllocator);
        operationHandlerRunnerPoolConfig.setBackgroundExpirationEnabled(false);
        operationHandlerRunnerPoolConfig.setPreciseLeakDetectionEnabled(false);
        operationHandlerRunnerPoolConfig.setExpiration(new NeverExpiration());
        this.operationHandlerRunnerPool = new BlazePool<>(operationHandlerRunnerPoolConfig);
        this.operationHandlerRunnerPool.setTargetSize(INITIAL_POOL_SIZE);
        this.highestSetPoolSize = INITIAL_POOL_SIZE;
    }

    @Override
    public OperationHandlerRunnableContext newOperationHandlerRunner() throws OperationException {
        try {
            OperationHandlerRunnableContext operationHandlerRunner =
                operationHandlerRunnerPool.claim(POOL_CLAIM_TIMEOUT);
            while (null == operationHandlerRunner) {
                int currentPoolSize = operationHandlerRunnerPool.getTargetSize();
                if (currentPoolSize < MAX_POOL_SIZE) {
                    operationHandlerRunnerPool.setTargetSize(currentPoolSize * 2);
                    highestSetPoolSize = currentPoolSize * 2;
                }
                operationHandlerRunner = operationHandlerRunnerPool.claim(POOL_CLAIM_AFTER_RESIZE_TIMEOUT);
            }
            return operationHandlerRunner;
        } catch (Exception e) {
            int currentPoolSize = operationHandlerRunnerPool.getTargetSize();
            throw new OperationException(
                format("Error encountered while attempting to allocate handler runner from pool\n"
                        + "Max pool size: %s\n"
                        + "Highest set pool size: %s\n"
                        + "Current pool size: %s",
                    MAX_POOL_SIZE,
                    highestSetPoolSize,
                    currentPoolSize),
                e
            );
        }
    }

    @Override
    public void shutdown() throws OperationException {
        TemporalUtil temporalUtil = new TemporalUtil();
        innerOperationHandlerRunnerFactory.shutdown();
        Completion completion = operationHandlerRunnerPool.shutdown();
        try {
            boolean isSuccessfulShutdown = completion.await(POOL_SHUTDOWN_TIMEOUT);
            if (!isSuccessfulShutdown) {
                throw new OperationException(
                    format(
                        "Operation handler pool did not shutdown before timeout: %s\n"
                            + "Pool Target Size: %s\n"
                            + "Pool Allocation Count: %s\n"
                            + "Pool Failed Allocation Count: %s\n"
                            + "Pool Leaked Objects Count: %s\n",
                        temporalUtil.milliDurationToString(TimeUnit.MILLISECONDS
                            .convert(POOL_SHUTDOWN_TIMEOUT.getTimeout(),
                                POOL_SHUTDOWN_TIMEOUT.getUnit())),
                        operationHandlerRunnerPool.getTargetSize(),
                        operationHandlerRunnerPool.getAllocationCount(),
                        operationHandlerRunnerPool.getFailedAllocationCount(),
                        operationHandlerRunnerPool.getLeakedObjectsCount()
                    // TODO percentile stats require MetricsRecorder implementation to work
                    // TODO http://chrisvest.github.io/stormpot/site/apidocs/stormpot/MetricsRecorder.html
                    /*
                     operationHandlerRunnerPool.getAllocationLatencyPercentile(90),
                     operationHandlerRunnerPool.getAllocationLatencyPercentile(99),
                     operationHandlerRunnerPool.getAllocationLatencyPercentile(100),
                     operationHandlerRunnerPool.getDeallocationLatencyPercentile(90),
                     operationHandlerRunnerPool.getDeallocationLatencyPercentile(99),
                     operationHandlerRunnerPool.getDeallocationLatencyPercentile(100),
                     operationHandlerRunnerPool.getAllocationFailureLatencyPercentile(90),
                     operationHandlerRunnerPool.getAllocationFailureLatencyPercentile(99),
                     operationHandlerRunnerPool.getAllocationFailureLatencyPercentile(100),
                     operationHandlerRunnerPool.getObjectLifetimePercentile(90),
                     operationHandlerRunnerPool.getObjectLifetimePercentile(99),
                     operationHandlerRunnerPool.getObjectLifetimePercentile(100),
                     operationHandlerRunnerPool.getReallocationFailureLatencyPercentile(90),
                     operationHandlerRunnerPool.getReallocationFailureLatencyPercentile(99),
                     operationHandlerRunnerPool.getReallocationFailureLatencyPercentile(100)
                     */
                    )
                );
            }
        } catch (InterruptedException e) {
            throw new OperationException("Error encountered while shutting down operation handler pool", e);
        }
    }

    @Override
    public String toString() {
        return PoolingOperationHandlerRunnerFactory.class.getSimpleName() + "{"
            + innerOperationHandlerRunnerFactory.toString() + "}";
    }

    private static class OperationHandlerRunnerAllocator implements Allocator<OperationHandlerRunnableContext> {
        private final OperationHandlerRunnerFactory operationHandlerRunnerFactory;

        public OperationHandlerRunnerAllocator(OperationHandlerRunnerFactory operationHandlerRunnerFactory) {
            this.operationHandlerRunnerFactory = operationHandlerRunnerFactory;
        }

        @Override
        public OperationHandlerRunnableContext allocate(Slot slot) throws Exception {
            OperationHandlerRunnableContext operationHandlerRunner =
                operationHandlerRunnerFactory.newOperationHandlerRunner();
            operationHandlerRunner.setSlot(slot);
            return operationHandlerRunner;
        }

        @Override
        public void deallocate(OperationHandlerRunnableContext operationHandlerRunner) throws Exception {
            // I think nothing needs to be done here
        }
    }

    private static class NeverExpiration implements Expiration<OperationHandlerRunnableContext> {
        @Override
        public boolean hasExpired(SlotInfo<? extends OperationHandlerRunnableContext> slotInfo) throws Exception {
            return false;
        }
    }
}
