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
