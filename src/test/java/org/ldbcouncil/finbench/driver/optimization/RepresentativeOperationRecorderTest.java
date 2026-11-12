/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package org.ldbcouncil.finbench.driver.optimization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.ldbcouncil.finbench.driver.Operation;

public class RepresentativeOperationRecorderTest {
    @Test
    public void shouldRetainAParameterizedCopyOfTheFirstOperation() {
        RepresentativeOperationRecorder recorder = new RepresentativeOperationRecorder();
        TestOperation first = new TestOperation(42);

        recorder.record(first);
        recorder.record(new TestOperation(99));

        Operation recorded = recorder.operation("TestOperation").get();
        assertNotSame(first, recorded);
        assertEquals(42, recorded.parameterMap().get("value"));
        assertEquals(TestOperation.class, recorder.operationTypeToClassMapping().get(1));
    }

    private static class TestOperation extends Operation<Object> {
        private final int value;

        TestOperation(int value) {
            this.value = value;
        }

        @Override
        public int type() {
            return 1;
        }

        @Override
        public Map<String, Object> parameterMap() {
            return Collections.<String, Object>singletonMap("value", value);
        }

        @Override
        public Object deserializeResult(String serializedOperationResult) throws IOException {
            return serializedOperationResult;
        }

        @Override
        public TestOperation newInstance() {
            return new TestOperation(value);
        }
    }
}
