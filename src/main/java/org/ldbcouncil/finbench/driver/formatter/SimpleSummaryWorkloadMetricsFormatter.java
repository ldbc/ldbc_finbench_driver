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

package org.ldbcouncil.finbench.driver.formatter;

import com.google.common.collect.Lists;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.ldbcouncil.finbench.driver.runtime.metrics.OperationMetricsSnapshot;
import org.ldbcouncil.finbench.driver.runtime.metrics.OperationTypeMetricsManager;
import org.ldbcouncil.finbench.driver.runtime.metrics.WorkloadResultsSnapshot;
import org.ldbcouncil.finbench.driver.temporal.TemporalUtil;

public class SimpleSummaryWorkloadMetricsFormatter implements WorkloadMetricsFormatter {
    private static final String DEFAULT_NAME = "<no name given>";
    private static final String DEFAULT_UNIT = "<no unit given>";
    private static final String OFFSET = "    ";
    private static final DecimalFormat INTEGER_FORMATTER = new DecimalFormat("###,###,###,###");
    private static final DecimalFormat FLOAT_FORMATTER = new DecimalFormat("###,###,###,##0.00");
    private static final TemporalUtil TEMPORAL_UTIL = new TemporalUtil();

    public String format(WorkloadResultsSnapshot resultsSnapshot) {
        List<OperationMetricsSnapshot> sortedMetrics = Lists.newArrayList(resultsSnapshot.allMetrics());
        Collections.sort(sortedMetrics, new OperationTypeMetricsManager.OperationMetricsNameComparator());

        int padRightDistance = 40;
        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------------------------------------------------------\n");
        sb
            .append(String.format("%1$-" + padRightDistance + "s", "Operation Count:"))
            .append(INTEGER_FORMATTER.format(resultsSnapshot.totalOperationCount()))
            .append("\n");
        sb
            .append(String.format("%1$-" + padRightDistance + "s", "Duration:"))
            .append(TEMPORAL_UTIL.nanoDurationToString(resultsSnapshot.totalRunDurationAsNano()))
            .append("\n");
        double opsPerNs = (resultsSnapshot.totalOperationCount() / (double) resultsSnapshot.totalRunDurationAsNano());
        double opsPerS = opsPerNs * TimeUnit.SECONDS.toNanos(1);
        sb
            .append(String.format("%1$-" + padRightDistance + "s", "Throughput:"))
            .append(FLOAT_FORMATTER.format(opsPerS))
            .append(" (op/s)\n");
        sb.append("------------------------------------------------------------------------------\n");
        int namePadRightDistance = 0;
        int countPadRightDistance = 0;
        for (OperationMetricsSnapshot metric : sortedMetrics) {
            namePadRightDistance = Math.max(namePadRightDistance, metric.name().length());
            countPadRightDistance = Math.max(countPadRightDistance, Long.toString(metric.count()).length());
        }
        for (OperationMetricsSnapshot metric : sortedMetrics) {
            sb.append(formatOneMetricRuntime(OFFSET, metric, namePadRightDistance + 2, countPadRightDistance + 2));
        }
        sb.append("------------------------------------------------------------------------------\n");
        return sb.toString();
    }

    private String formatOneMetricRuntime(String offset, OperationMetricsSnapshot metric, int namePadRightDistance,
                                          int countPadRightDistance) {
        String name = (null == metric.name()) ? DEFAULT_NAME : metric.name();
        String unit = (null == metric.durationUnit()) ? DEFAULT_UNIT
            : TEMPORAL_UTIL.abbreviatedTimeUnit(metric.durationUnit());
        return offset
            + String.format("%1$-" + namePadRightDistance + "s", name)
            + "Count: "
            + String.format("%1$-" + countPadRightDistance + "s",
            INTEGER_FORMATTER.format(metric.runTimeMetric().count()))
            + " "
            + "Mean: "
            + FLOAT_FORMATTER.format(metric.runTimeMetric().mean()) + " " + unit
            + "\n";
    }
}
