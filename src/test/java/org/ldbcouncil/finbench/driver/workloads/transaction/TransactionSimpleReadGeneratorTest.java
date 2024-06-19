package org.ldbcouncil.finbench.driver.workloads.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class TransactionSimpleReadGeneratorTest {
    private LdbcFinBenchSimpleReadGenerator simpleReadGenerator;

    @BeforeEach
    public void init() {
        simpleReadGenerator = mock(LdbcFinBenchSimpleReadGenerator.class);
    }

    // Test with a very small duration
    @Test
    public void generateExponentialRandomValue_SmallDuration() {
        double uniformRandomValue = 0.5;
        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() +  + 1000L * 60 * 60 * 24);

        when(simpleReadGenerator.generateExponentialRandomValue(eq(uniformRandomValue), eq(startTime), eq(endTime)))
                .thenCallRealMethod();

        double value = simpleReadGenerator.generateExponentialRandomValue(uniformRandomValue, startTime, endTime);

        verify(simpleReadGenerator).generateExponentialRandomValue(eq(uniformRandomValue), eq(startTime), eq(endTime));

        assertEquals(0.9772, value, 0.001);
    }

    // Test with a duration equal to UNIFORMITY_DURATION
    @Test
    public void generateExponentialRandomValue_UniformityDuration() {
        double uniformRandomValue = 0.5;
        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() + LdbcFinBenchSimpleReadGenerator.UNIFORMITY_DURATION);

        when(simpleReadGenerator.generateExponentialRandomValue(eq(uniformRandomValue), eq(startTime), eq(endTime)))
                .thenCallRealMethod();

        double value = simpleReadGenerator.generateExponentialRandomValue(uniformRandomValue, startTime, endTime);

        verify(simpleReadGenerator).generateExponentialRandomValue(eq(uniformRandomValue), eq(startTime), eq(endTime));

        assertEquals(0.5, value, 0.001);
    }

    // Test with a very large duration
    @Test
    public void generateExponentialRandomValue_LargeDuration() {
        double uniformRandomValue = 0.5;
        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() + 1000L * 60 * 60 * 24 * 365);

        when(simpleReadGenerator.generateExponentialRandomValue(eq(uniformRandomValue), eq(startTime), eq(endTime)))
                .thenCallRealMethod();

        double value = simpleReadGenerator.generateExponentialRandomValue(uniformRandomValue, startTime, endTime);

        verify(simpleReadGenerator).generateExponentialRandomValue(eq(uniformRandomValue), eq(startTime), eq(endTime));

        assertEquals(0.0554, value, 0.001);
    }

    // Helper method to calculate the expected value based on the real method logic
    private double calculateExpectedValue(double uniformRandomValue, Date startTime, Date endTime) {
        long duration = endTime.getTime() - startTime.getTime();
        double scale;
        if (LdbcFinBenchSimpleReadGenerator.UNIFORMITY_DURATION < duration) {
            scale = 1.0 * LdbcFinBenchSimpleReadGenerator.UNIFORMITY_DURATION / duration;
        } else {
            scale = 1.0 * duration / LdbcFinBenchSimpleReadGenerator.UNIFORMITY_DURATION;
        }
        double adjustedValue;
        if (LdbcFinBenchSimpleReadGenerator.UNIFORMITY_DURATION < duration) {
            adjustedValue = 1 - Math.pow(uniformRandomValue, scale);
        } else {
            adjustedValue = Math.pow(uniformRandomValue, scale);
        }
        return adjustedValue;
    }
}
