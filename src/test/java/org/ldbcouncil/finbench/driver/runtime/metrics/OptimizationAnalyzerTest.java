/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.runtime.metrics;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.ldbcouncil.finbench.driver.optimization.OptimizationAnalyzer;
import org.ldbcouncil.finbench.driver.optimization.OptimizationCandidate;

public class OptimizationAnalyzerTest {
    @Test
    public void shouldRankOperationWithLargestServiceTimeFirst() {
        OperationMetricsSnapshot frequent = metric("Frequent", 100, 10, 12);
        OperationMetricsSnapshot slow = metric("Slow", 2, 100, 120);
        WorkloadResultsSnapshot results =
            new WorkloadResultsSnapshot(
                Arrays.asList(frequent, slow), 0, 1000, 102, TimeUnit.MILLISECONDS);

        List<OptimizationCandidate> candidates = new OptimizationAnalyzer().rank(results);

        assertThat(candidates.get(0).operation(), equalTo("Frequent"));
        assertThat(
            candidates.get(0).serviceTimeShare(),
            greaterThan(candidates.get(1).serviceTimeShare()));
    }

    private static OperationMetricsSnapshot metric(String name, long count, long mean, long p95) {
        ContinuousMetricSnapshot runtime = new ContinuousMetricSnapshot(
            "Runtime",
            TimeUnit.MILLISECONDS,
            count,
            mean,
            1,
            p95,
            mean,
            mean,
            mean,
            mean,
            p95,
            p95,
            p95,
            1
        );
        return new OperationMetricsSnapshot(name, TimeUnit.MILLISECONDS, count, runtime);
    }
}
