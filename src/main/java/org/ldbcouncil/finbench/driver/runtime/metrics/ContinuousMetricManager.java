package org.ldbcouncil.finbench.driver.runtime.metrics;

import java.util.concurrent.TimeUnit;
import org.HdrHistogram.Histogram;

/*
http://giltene.github.io/HdrHistogram/JavaDoc/
Specifying 3 decimal points of precision in this example guarantees that value quantization within the value range will
be no larger than 1/1,000th (or 0.1%) of any recorded value. This example Histogram can be therefor used to track,
analyze and report the counts of observed latencies ranging between 1 microsecond and 1 hour in magnitude, while
maintaining a value resolution 1 microsecond (or better) up to 1 millisecond, a resolution of 1 millisecond (or better)
up to one second, and a resolution of 1 second (or better) up to 1,000 seconds. At it's maximum tracked value (1 hour),
it would still maintain a resolution of 3.6 seconds (or better).
 */
public class ContinuousMetricManager {
    private final Histogram histogram;
    private final String name;
    private final TimeUnit unit;

    public ContinuousMetricManager(String name, TimeUnit unit, long highestExpectedValue,
                                   int numberOfSignificantDigits) {
        long lowestExpectedValue = 1;
        histogram = new Histogram(lowestExpectedValue, highestExpectedValue, numberOfSignificantDigits);
        this.name = name;
        this.unit = unit;
    }

    public void addMeasurement(long value) {
        histogram.recordValue(value);
    }

    public ContinuousMetricSnapshot snapshot() {
        return new ContinuousMetricSnapshot(
            name,
            unit,
            count(),
            mean(),
            min(),
            max(),
            percentile25(),
            percentile50(),
            percentile75(),
            percentile90(),
            percentile95(),
            percentile99(),
            percentile99_9(),
            stdDev());
    }

    private long count() {
        return histogram.getTotalCount();
    }

    private double mean() {
        if (0 == count()) {
            return -1;
        }
        return histogram.getMean();
    }

    private long min() {
        if (0 == count()) {
            return -1;
        }
        return histogram.getMinValue();
    }

    private long max() {
        if (0 == count()) {
            return -1;
        }
        return histogram.getMaxValue();
    }

    private long percentile25() {
        if (0 == count()) {
            return -1;
        }
        return histogram.getValueAtPercentile(25);
    }

    private long percentile50() {
        if (0 == count()) {
            return -1;
        }
        return histogram.getValueAtPercentile(50);
    }

    private long percentile75() {
        if (0 == count()) {
            return -1;
        }
        return histogram.getValueAtPercentile(75);
    }

    private long percentile90() {
        if (0 == count()) {
            return -1;
        }
        return histogram.getValueAtPercentile(90);
    }

    private long percentile95() {
        if (0 == count()) {
            return -1;
        }
        return histogram.getValueAtPercentile(95);
    }

    private long percentile99() {
        if (0 == count()) {
            return -1;
        }
        return histogram.getValueAtPercentile(99);
    }

    private long percentile99_9() {
        if (0 == count()) {
            return -1;
        }
        return histogram.getValueAtPercentile(99.9);
    }

    private double stdDev() {
        if (0 == count()) {
            return -1;
        }
        return histogram.getStdDeviation();
    }
}
