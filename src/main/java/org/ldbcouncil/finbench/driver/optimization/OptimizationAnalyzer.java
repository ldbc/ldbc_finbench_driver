/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.ldbcouncil.finbench.driver.runtime.metrics.OperationMetricsSnapshot;
import org.ldbcouncil.finbench.driver.runtime.metrics.WorkloadResultsSnapshot;

public class OptimizationAnalyzer {
    public List<OptimizationCandidate> rank(WorkloadResultsSnapshot results) {
        double totalServiceTimeMillis = 0;
        double maxP95Millis = 0;
        for (OperationMetricsSnapshot metric : results.allMetrics()) {
            double meanMillis = toMillis(metric.runTimeMetric().mean(), metric.durationUnit());
            totalServiceTimeMillis += metric.count() * meanMillis;
            maxP95Millis = Math.max(
                maxP95Millis,
                toMillis(metric.runTimeMetric().percentile95(), metric.durationUnit())
            );
        }

        List<OptimizationCandidate> candidates = new ArrayList<>();
        for (OperationMetricsSnapshot metric : results.allMetrics()) {
            double meanMillis = toMillis(metric.runTimeMetric().mean(), metric.durationUnit());
            double p95Millis = toMillis(metric.runTimeMetric().percentile95(), metric.durationUnit());
            double serviceTime = metric.count() * meanMillis;
            double share = totalServiceTimeMillis == 0 ? 0 : serviceTime / totalServiceTimeMillis;
            double normalizedP95 = maxP95Millis == 0 ? 0 : p95Millis / maxP95Millis;
            double score = share * 0.8 + normalizedP95 * 0.2;
            candidates.add(new OptimizationCandidate(
                metric.name(),
                metric.count(),
                meanMillis,
                p95Millis,
                serviceTime,
                share,
                score
            ));
        }
        candidates.sort(Comparator.comparingDouble(OptimizationAnalyzer::score).reversed());
        return candidates;
    }

    private static double score(OptimizationCandidate candidate) {
        return candidate.recommendationScore();
    }

    private static double toMillis(double duration, TimeUnit unit) {
        return duration * unit.toNanos(1) / (double) TimeUnit.MILLISECONDS.toNanos(1);
    }
}
