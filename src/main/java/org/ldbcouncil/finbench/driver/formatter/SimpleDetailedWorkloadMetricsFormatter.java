package org.ldbcouncil.finbench.driver.formatter;

import com.google.common.collect.Lists;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.ldbcouncil.finbench.driver.runtime.metrics.OperationMetricsSnapshot;
import org.ldbcouncil.finbench.driver.runtime.metrics.OperationTypeMetricsManager;
import org.ldbcouncil.finbench.driver.runtime.metrics.WorkloadResultsSnapshot;
import org.ldbcouncil.finbench.driver.temporal.TemporalUtil;

public class SimpleDetailedWorkloadMetricsFormatter implements WorkloadMetricsFormatter {
    private static final String DEFAULT_NAME = "<no name given>";
    private static final String DEFAULT_UNIT = "<no unit given>";
    private static final String OFFSET = "    ";
    private static final DecimalFormat INTEGER_FORMATTER = new DecimalFormat("###,###,###,###");
    private static final DecimalFormat FLOAT_FORMATTER = new DecimalFormat("###,###,###,##0.00");
    private static final TemporalUtil TEMPORAL_UTIL = new TemporalUtil();
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss.SSS");

    public String format(WorkloadResultsSnapshot workloadResultsSnapshot) {
        List<OperationMetricsSnapshot> sortedMetrics = Lists.newArrayList(workloadResultsSnapshot.allMetrics());
        Collections.sort(sortedMetrics, new OperationTypeMetricsManager.OperationMetricsNameComparator());

        int padRightDistance = 40;
        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------------------------------------------------------\n");
        sb.append(String.format("%1$-" + padRightDistance + "s", "Operation Count:"))
            .append(INTEGER_FORMATTER.format(workloadResultsSnapshot.totalOperationCount())).append("\n");
        sb.append(String.format("%1$-" + padRightDistance + "s", "Duration:"))
            .append(TEMPORAL_UTIL.nanoDurationToString(workloadResultsSnapshot.totalRunDurationAsNano()))
            .append("\n");
        double opsPerNs =
            (workloadResultsSnapshot.totalOperationCount() / (double) workloadResultsSnapshot.totalRunDurationAsNano());
        double opsPerS = opsPerNs * TimeUnit.SECONDS.toNanos(1);
        sb.append(String.format("%1$-" + padRightDistance + "s", "Throughput:"))
            .append(FLOAT_FORMATTER.format(opsPerS)).append(" (op/s)\n");
        sb.append(String.format("%1$-" + padRightDistance + "s",
            "Start Time (" + TimeZone.getDefault().getDisplayName() + "):"))
            .append(dateTimeFormat.format(workloadResultsSnapshot.startTimeAsMilli())).append("\n");
        sb.append(String.format("%1$-" + padRightDistance + "s",
            "Finish Time (" + TimeZone.getDefault().getDisplayName() + "):"))
            .append(dateTimeFormat.format(workloadResultsSnapshot.latestFinishTimeAsMilli())).append("\n");
        sb.append("------------------------------------------------------------------------------\n");
        for (OperationMetricsSnapshot metric : sortedMetrics) {
            sb.append(formatOneMetricRuntime(OFFSET, metric));
        }
        sb.append("------------------------------------------------------------------------------\n");
        return sb.toString();
    }

    private String formatOneMetricRuntime(String offset, OperationMetricsSnapshot metric) {
        int padRightDistance = 20;
        String name = (null == metric.name()) ? DEFAULT_NAME : metric.name();
        String unit = (null == metric.durationUnit()) ? DEFAULT_UNIT : metric.durationUnit().toString();
        String sb = offset + String.format("%s\n", name)
            + offset + offset + String.format("%1$-" + padRightDistance + "s", "Units:")
            + unit + "\n"
            + offset + offset + String.format("%1$-" + padRightDistance + "s", "Count:")
            + INTEGER_FORMATTER.format(metric.runTimeMetric().count()) + "\n"
            + offset + offset + String.format("%1$-" + padRightDistance + "s", "Min:")
            + INTEGER_FORMATTER.format(metric.runTimeMetric().min()) + "\n"
            + offset + offset + String.format("%1$-" + padRightDistance + "s", "Max:")
            + INTEGER_FORMATTER.format(metric.runTimeMetric().max()) + "\n"
            + offset + offset + String.format("%1$-" + padRightDistance + "s", "Mean:")
            + FLOAT_FORMATTER.format(metric.runTimeMetric().mean()) + "\n"
            + offset + offset
            + String.format("%1$-" + padRightDistance + "s", "50th Percentile:")
            + INTEGER_FORMATTER.format(metric.runTimeMetric().percentile50()) + "\n"
            + offset + offset
            + String.format("%1$-" + padRightDistance + "s", "90th Percentile:")
            + INTEGER_FORMATTER.format(metric.runTimeMetric().percentile90()) + "\n"
            + offset + offset
            + String.format("%1$-" + padRightDistance + "s", "95th Percentile:")
            + INTEGER_FORMATTER.format(metric.runTimeMetric().percentile95()) + "\n"
            + offset + offset
            + String.format("%1$-" + padRightDistance + "s", "99th Percentile:")
            + INTEGER_FORMATTER.format(metric.runTimeMetric().percentile99()) + "\n";
        return sb;
    }
}
