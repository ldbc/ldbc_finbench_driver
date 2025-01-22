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

package org.ldbcouncil.finbench.driver.workloads.transaction;

import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.csv.FileLoader;
import org.ldbcouncil.finbench.driver.generator.EventStreamReader;

/**
 * ReadOperationStream.java
 * Class to read the operation streams:
 * - Complex Read Query
 * - Write Query
 * - ReadWrite Query
 */

public class OperationStreamReader {

    private final FileLoader loader;

    public OperationStreamReader(FileLoader loader) {
        this.loader = loader;
    }

    public Iterator<Operation> readOperationStream(
        EventStreamReader.EventDecoder<Operation> decoder,
        File readOperationFile
    ) throws WorkloadException {
        Iterator<Operation> opStream;

        try {
            opStream = loader.loadOperationStream(readOperationFile.getAbsolutePath(), decoder);
        } catch (SQLException e) {
            throw new WorkloadException(
                "Error loading operation stream with path: " + readOperationFile.getAbsolutePath());
        }

        return opStream;

    }
}
