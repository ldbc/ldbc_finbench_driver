package org.ldbcouncil.finbench.driver.generator;

import java.util.Iterator;
import org.ldbcouncil.finbench.driver.util.Function1;

public class MappingGenerator<FROM_GENERATE_TYPE, TO_GENERATE_TYPE> extends Generator<TO_GENERATE_TYPE> {
    private final Iterator<FROM_GENERATE_TYPE> fromGenerator;
    private final Function1<FROM_GENERATE_TYPE, TO_GENERATE_TYPE, RuntimeException> mapFunction;

    public MappingGenerator(
        Iterator<FROM_GENERATE_TYPE> fromGenerator,
        Function1<FROM_GENERATE_TYPE, TO_GENERATE_TYPE, RuntimeException> mapFunction) {
        this.fromGenerator = fromGenerator;
        this.mapFunction = mapFunction;
    }

    @Override
    protected TO_GENERATE_TYPE doNext() throws GeneratorException {
        if (fromGenerator.hasNext()) {
            return mapFunction.apply(fromGenerator.next());
        } else {
            return null;
        }
    }
}
