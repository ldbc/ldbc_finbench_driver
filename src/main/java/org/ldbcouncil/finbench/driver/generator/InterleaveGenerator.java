package org.ldbcouncil.finbench.driver.generator;

import java.util.Iterator;
import org.ldbcouncil.finbench.driver.util.Function0;

public class InterleaveGenerator<GENERATE_TYPE> extends Generator<GENERATE_TYPE> {
    private final Iterator<? extends GENERATE_TYPE> baseGenerator;
    private final Iterator<? extends GENERATE_TYPE> interleaveFromGenerator;
    private final Function0<Integer, RuntimeException> amountToInterleaveFun;

    private int remainingToInterleave;

    InterleaveGenerator(Iterator<? extends GENERATE_TYPE> baseGenerator,
                        Iterator<? extends GENERATE_TYPE> interleaveFromGenerator,
                        Function0<Integer, RuntimeException> amountToInterleaveFun) {
        this.baseGenerator = baseGenerator;
        this.interleaveFromGenerator = interleaveFromGenerator;
        this.amountToInterleaveFun = amountToInterleaveFun;
        this.remainingToInterleave = 0;
    }

    @Override
    protected GENERATE_TYPE doNext() throws GeneratorException {
        if (0 == remainingToInterleave) {
            if (baseGenerator.hasNext()) {
                remainingToInterleave = amountToInterleaveFun.apply();
                return baseGenerator.next();
            } else {
                return null;
            }
        } else {
            if (interleaveFromGenerator.hasNext()) {
                remainingToInterleave--;
                return interleaveFromGenerator.next();
            } else {
                return null;
            }
        }
    }
}
