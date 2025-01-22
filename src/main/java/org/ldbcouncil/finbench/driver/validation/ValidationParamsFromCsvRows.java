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

package org.ldbcouncil.finbench.driver.validation;

import static java.lang.String.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.generator.GeneratorException;

public class ValidationParamsFromCsvRows implements Iterator<ValidationParam> {
    private final Iterator<String[]> csvRows;
    private final Workload workload;
    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public ValidationParamsFromCsvRows(Iterator<String[]> csvRows, Workload workload) {
        this.csvRows = csvRows;
        this.workload = workload;
    }

    @Override
    public boolean hasNext() {
        return csvRows.hasNext();
    }

    @Override
    public ValidationParam next() {
        String[] csvRow = csvRows.next();
        String serializedOperation = csvRow[0];
        String serializedOperationResult = csvRow[1];

        Operation operation;
        try {
            operation = OBJECT_MAPPER.readValue(serializedOperation, Operation.class);
        } catch (IOException e) {
            throw new GeneratorException(format("Error marshalling operation\n%s", serializedOperation), e);
        }

        Object operationResult;
        try {
            operationResult = operation.deserializeResult(serializedOperationResult);
        } catch (IOException e) {
            throw new GeneratorException(format("Error marshalling operation result\n%s", serializedOperationResult),
                e);
        }
        return ValidationParam.createUntyped(operation, operationResult);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() not supported by " + getClass().getName());
    }
}
