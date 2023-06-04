package org.ldbcouncil.finbench.driver.workloads.transaction;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.csv.FileLoader;
import org.ldbcouncil.finbench.driver.generator.EventStreamReader;
import org.ldbcouncil.finbench.driver.generator.GeneratorFactory;
import org.ldbcouncil.finbench.driver.util.Tuple2;

public class RunnableOperationStreamBatchLoader extends Thread {

    private final FileLoader loader;
    private final long batchSize;
    private final GeneratorFactory gf;
    private final File updatesDir;
    private final String fileSuffix;
    private final Set<Class<? extends Operation>> enabledUpdateOperationTypes;
    private final BlockingQueue<Iterator<Operation>> blockingQueue;
    private final CountDownLatch finishInit;

    public RunnableOperationStreamBatchLoader(
        FileLoader loader,
        GeneratorFactory gf,
        File updatesDir,
        String fileSuffix,
        BlockingQueue<Iterator<Operation>> blockingQueue,
        Set<Class<? extends Operation>> enabledUpdateOperationTypes,
        long batchSize,
        CountDownLatch finishInit
    ) {
        this.loader = loader;
        this.gf = gf;
        this.updatesDir = updatesDir;
        this.fileSuffix = fileSuffix;
        this.blockingQueue = blockingQueue;
        this.enabledUpdateOperationTypes = enabledUpdateOperationTypes;
        this.batchSize = batchSize;
        this.finishInit = finishInit;
    }

    /**
     * Loads the operation streams into the LinkedBlockingDeque
     */
    @Override
    public void run() {
        BatchedOperationStreamReader updateOperationStream = new BatchedOperationStreamReader(loader);
        Map<Class<? extends Operation>, String> classToPathMap =
            LdbcFinBenchTransactionWorkloadConfiguration.getUpdateStreamClassToPathMapping();
        Map<Class<? extends Operation>, String> classToBatchColumn =
            LdbcFinBenchTransactionWorkloadConfiguration.getUpdateStreamClassToDateColumn();
        long offset = Long.MAX_VALUE;
        Map<Class<? extends Operation>, Long> classToLastValue = new HashMap<>();
        try {
            for (Class<? extends Operation> enabledClass : enabledUpdateOperationTypes) {
                String filename = classToPathMap.get(enabledClass);
                String batchColumn = classToBatchColumn.get(enabledClass);
                String viewName = enabledClass.getSimpleName();
                // Initialize the batch reader to set the view in DuckDB on the parquet file
                Tuple2<Long, Long> boundaries = updateOperationStream.init(
                    new File(updatesDir, filename
                        + LdbcFinBenchTransactionWorkloadConfiguration.FILE_SEPARATOR + fileSuffix),
                    viewName,
                    batchColumn
                );

                classToLastValue.put(enabledClass, boundaries._2());

                // avoid empty incremental lead to file offset = -1
                if (boundaries._1() != -1 && boundaries._1() < offset) {
                    offset = boundaries._1();
                }
            }

            // Loop until interrupt or no operations left to load
            while (!Thread.interrupted()) {
                Iterator<Operation> newBatch = loadNextBatch(
                    updateOperationStream,
                    offset,
                    classToLastValue
                );
                if (!newBatch.hasNext()) {
                    // No new operations, stream empty.
                    return;
                }
                // Waits for a free slot.
                blockingQueue.put(newBatch);
                offset = offset + batchSize;
                if (finishInit.getCount() == 1) {
                    finishInit.countDown();
                }
            }
        } catch (WorkloadException | SQLException ew) {
            ew.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (finishInit.getCount() == 1) {
                finishInit.countDown();
            }
        }
    }

    /**
     * Fetches the next batch of operation streams
     *
     * @param updateOperationStream
     * @param offset
     * @param classtoEndValue
     * @return
     */
    private Iterator<Operation> loadNextBatch(
        BatchedOperationStreamReader updateOperationStream,
        long offset,
        Map<Class<? extends Operation>, Long> classtoEndValue
    ) throws SQLException, WorkloadException {
        Map<Class<? extends Operation>, String> classToBatchColumn =
            LdbcFinBenchTransactionWorkloadConfiguration.getUpdateStreamClassToDateColumn();
        ArrayList<Iterator<Operation>> listOfBatchedOperationStreams = new ArrayList<>();

        Map<Class<? extends Operation>, EventStreamReader.EventDecoder<Operation>> decoders =
            UpdateEventStreamReader.getDecoders();
        for (Class<? extends Operation> enabledClass : enabledUpdateOperationTypes) {
            String batchColumn = classToBatchColumn.get(enabledClass);
            String viewName = enabledClass.getSimpleName();
            if (offset <= classtoEndValue.get(enabledClass)) {
                Iterator<Operation> operationStream = updateOperationStream.readBatchedOperationStream(
                    decoders.get(enabledClass),
                    offset,
                    batchSize,
                    viewName,
                    batchColumn
                );
                // Update offset
                // Only put non-empty iterators in the list to merge
                if (operationStream.hasNext()) {
                    listOfBatchedOperationStreams.add(operationStream);
                }
            }
        }
        // If empty, it means there is nothing more to load.
        if (listOfBatchedOperationStreams.isEmpty()) {
            return Collections.emptyIterator();
        }
        // Merge the operation streams and sort them by timestamp
        Iterator<Operation> mergedUpdateStreams = Collections.<Operation>emptyIterator();
        for (Iterator<Operation> updateStream : listOfBatchedOperationStreams) {
            mergedUpdateStreams = gf.mergeSortOperationsByTimeStamp(mergedUpdateStreams, updateStream);
        }
        return mergedUpdateStreams;
    }
}
