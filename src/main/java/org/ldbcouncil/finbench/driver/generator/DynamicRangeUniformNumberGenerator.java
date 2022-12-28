package org.ldbcouncil.finbench.driver.generator;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.ldbcouncil.finbench.driver.util.NumberHelper;

public class DynamicRangeUniformNumberGenerator<GENERATE_TYPE extends Number> extends Generator<GENERATE_TYPE> {
    private final MinMaxGenerator<GENERATE_TYPE> lowerBoundGenerator;
    private final MinMaxGenerator<GENERATE_TYPE> upperBoundGenerator;
    private final NumberHelper<GENERATE_TYPE> number;
    private final RandomDataGenerator random;

    DynamicRangeUniformNumberGenerator(RandomDataGenerator random,
                                       MinMaxGenerator<GENERATE_TYPE> lowerBoundGenerator,
                                       MinMaxGenerator<GENERATE_TYPE> upperBoundGenerator) {
        this.random = random;
        this.lowerBoundGenerator = lowerBoundGenerator;
        this.upperBoundGenerator = upperBoundGenerator;
        this.number = NumberHelper.createNumberHelper(lowerBoundGenerator.getMin().getClass());
    }

    @Override
    protected GENERATE_TYPE doNext() throws GeneratorException {
        return number.uniform(random, lowerBoundGenerator.getMin(), upperBoundGenerator.getMax());
    }
}
