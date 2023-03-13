package org.ldbcouncil.finbench.driver.runtime.metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.ldbcouncil.finbench.driver.util.Bucket;
import org.ldbcouncil.finbench.driver.util.Histogram;

public class DiscreteMetricManager {
    private final Histogram<Long, Integer> measurements;
    private final String name;
    private final String unit;
    private long measurementMin = Long.MAX_VALUE;
    private long measurementMax = Long.MIN_VALUE;

    public DiscreteMetricManager(String name, String unit) {
        this.measurements = new Histogram<>(0);
        this.name = name;
        this.unit = unit;
    }

    public void addMeasurement(long value) {
        measurements.incOrCreateBucket(Bucket.DiscreteBucket.create(value), 1);
        measurementMin = Math.min(value, measurementMin);
        measurementMax = Math.max(value, measurementMax);
    }

    public DiscreteMetricSnapshot snapshot() {
        return new DiscreteMetricSnapshot(name, unit, count(), allValues());
    }

    private long count() {
        return measurements.sumOfAllBucketValues();
    }

    private Map<Long, Long> allValues() {
        Map<Long, Long> allValuesMap = new HashMap<Long, Long>();
        for (Entry<Bucket<Long>, Integer> entry : measurements.getAllBuckets()) {
            long resultCode = ((Bucket.DiscreteBucket<Long>) entry.getKey()).getId();
            long resultCodeCount = entry.getValue();
            allValuesMap.put(resultCode, resultCodeCount);
        }
        return allValuesMap;
    }
}
