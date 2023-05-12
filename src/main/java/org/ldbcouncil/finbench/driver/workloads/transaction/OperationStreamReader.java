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
