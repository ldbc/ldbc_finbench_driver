package org.ldbcouncil.finbench.driver.workloads.transaction;

import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.csv.FileLoader;
import org.ldbcouncil.finbench.driver.generator.EventStreamReader;
import org.ldbcouncil.finbench.driver.util.Tuple2;

public class BatchedOperationStreamReader {

    private final FileLoader loader;

    public BatchedOperationStreamReader(
        FileLoader loader
    ) {
        this.loader = loader;
    }

    public Tuple2<Long, Long> init(
        File operationFile,
        String viewName,
        String batchColumn
    ) throws WorkloadException, SQLException {
        loader.createViewOnParquetFile(
            operationFile.getAbsolutePath(),
            viewName
        );

        return loader.getBoundaryValues(batchColumn, viewName);
    }

    public Iterator<Operation> readBatchedOperationStream(
        EventStreamReader.EventDecoder<Operation> decoder,
        long offset,
        long batchSize,
        String viewName,
        String batchColumnName
    ) throws WorkloadException {
        Iterator<Operation> opStream;

        try {
            opStream = loader.getOperationStreamBatch(decoder, viewName, batchColumnName, offset, batchSize);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new WorkloadException("Error loading batched operation stream with view: " + viewName);
        }
        return opStream;
    }
}
