/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.driver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.ldbcouncil.finbench.driver.DbException;
import org.ldbcouncil.finbench.driver.optimization.OperationResourceProfile;
import org.ldbcouncil.finbench.driver.optimization.OptimizationCandidate;
import org.ldbcouncil.finbench.driver.optimization.OptimizationSupport;
import org.ldbcouncil.finbench.driver.optimization.SyntheticExecutionResult;
import org.ldbcouncil.finbench.driver.optimization.SyntheticLoadSpec;

public class OptimizationRecommendationModeTest {
    @Test
    public void shouldProfileMemoryGrowthOverBaseline() throws Exception {
        OptimizationCandidate candidate =
            new OptimizationCandidate("ComplexRead1", 1, 30, 30, 30, 1, 1);
        TestOptimizationSupport support = new TestOptimizationSupport();

        OperationResourceProfile profile =
            OptimizationRecommendationMode.profileOperation(
                support,
                candidate,
                2,
                1,
                support::executeRepresentativeOperation);

        assertEquals(300, profile.averageMemoryBytes(), 0);
        assertEquals(300, profile.peakMemoryBytes());
        assertEquals(0.5, profile.averageDurationMillis(), 0);
        assertTrue(profile.averageCpuCores() > 0);
    }

    @Test
    public void shouldResetBeforeEachResourceSampleAndRepresentativeExecution() throws Exception {
        AtomicInteger resets = new AtomicInteger();
        AtomicInteger executions = new AtomicInteger();
        TestOptimizationSupport support = new TestOptimizationSupport() {
            @Override
            public double getCurrentCpuCores() {
                assertTrue(resets.get() > 0);
                return super.getCurrentCpuCores();
            }
        };
        OperationResourceProfile profile = OptimizationRecommendationMode.profileOperation(
            support, new OptimizationCandidate("Write1", 1, 30, 30, 30, 1, 1), 3, 1,
            () -> resets.incrementAndGet(),
            () -> {
                assertEquals(executions.incrementAndGet(), resets.get());
                return new SyntheticExecutionResult(500000, 0);
            });
        assertEquals(3, resets.get());
        assertEquals(3, executions.get());
        assertEquals(0.5, profile.averageDurationMillis(), 0);
    }

    @Test
    public void shouldStopBeforeSamplingWhenRecoveryFails() {
        TestOptimizationSupport support = new TestOptimizationSupport() {
            @Override
            public double getCurrentCpuCores() {
                throw new AssertionError("Sampling must not start after recovery failure");
            }
        };
        DbException failure = assertThrows(DbException.class,
            () -> OptimizationRecommendationMode.profileOperation(
                support, new OptimizationCandidate("Write1", 1, 30, 30, 30, 1, 1), 3, 1,
                () -> {
                    throw new DbException("recovery failed");
                },
                () -> {
                    throw new AssertionError("Operation must not run");
                }));
        assertEquals("recovery failed", failure.getMessage());
    }

    private static class TestOptimizationSupport implements OptimizationSupport {
        private volatile boolean representativeOperationRunning;

        @Override
        public double getCurrentCpuCores() {
            return representativeOperationRunning ? 1.5 : 1;
        }

        @Override
        public long getCurrentMemoryBytes() {
            return representativeOperationRunning ? 1300 : 1000;
        }

        SyntheticExecutionResult executeRepresentativeOperation() throws DbException {
            representativeOperationRunning = true;
            try {
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new DbException("Representative operation interrupted", e);
            } finally {
                representativeOperationRunning = false;
            }
            return new SyntheticExecutionResult(TimeUnit.MICROSECONDS.toNanos(500), 250);
        }

        @Override
        public SyntheticExecutionResult executeSyntheticLoad(SyntheticLoadSpec spec) {
            throw new UnsupportedOperationException();
        }
    }
}
