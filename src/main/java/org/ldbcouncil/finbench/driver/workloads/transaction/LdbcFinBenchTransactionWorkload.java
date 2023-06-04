package org.ldbcouncil.finbench.driver.workloads.transaction;

import static java.lang.String.format;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.ldbcouncil.finbench.driver.ChildOperationGenerator;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.WorkloadStreams;
import org.ldbcouncil.finbench.driver.control.ConsoleAndFileDriverConfiguration;
import org.ldbcouncil.finbench.driver.control.OperationMode;
import org.ldbcouncil.finbench.driver.csv.DuckDbExtractor;
import org.ldbcouncil.finbench.driver.csv.FileLoader;
import org.ldbcouncil.finbench.driver.generator.BufferedIterator;
import org.ldbcouncil.finbench.driver.generator.EventStreamReader;
import org.ldbcouncil.finbench.driver.generator.GeneratorFactory;
import org.ldbcouncil.finbench.driver.generator.OperationStreamBuffer;
import org.ldbcouncil.finbench.driver.generator.RandomDataGeneratorFactory;
import org.ldbcouncil.finbench.driver.util.ClassLoaderHelper;
import org.ldbcouncil.finbench.driver.util.ClassLoadingException;
import org.ldbcouncil.finbench.driver.util.MapUtils;
import org.ldbcouncil.finbench.driver.util.Tuple4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead10;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead11;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead12;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead6;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead7;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead8;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead9;

public class LdbcFinBenchTransactionWorkload extends Workload {
    private Map<Integer, Long> longReadInterleavesAsMilli;
    private File parametersDir;
    private File updatesDir;
    private String fileSuffix;
    private long updateInterleaveAsMilli;
    private double compressionRatio;
    private double simpleReadDissipationFactor;
    private OperationMode operationMode;
    private double batchSize;
    private Set<Class<? extends Operation>> enabledLongReadOperationTypes;
    private Set<Class<? extends Operation>> enabledSimpleReadOperationTypes;
    private Set<Class<? extends Operation>> enabledWriteOperationTypes;
    private Set<Class<? extends Operation>> enabledDeleteOperationTypes;
    private Set<Class<? extends Operation>> enabledUpdateOperationTypes;

    private RunnableOperationStreamBatchLoader runnableBatchLoader;

    @Override
    public Map<Integer, Class<? extends Operation>> operationTypeToClassMapping() {
        return LdbcFinBenchTransactionWorkloadConfiguration.operationTypeToClassMapping();
    }

    @Override
    public void onInit(Map<String, String> params) throws WorkloadException {
        List<String> compulsoryKeys = Lists.newArrayList();

        // Check operation mode, default is execute_benchmark
        if (params.containsKey(ConsoleAndFileDriverConfiguration.MODE_ARG)) {
            operationMode = OperationMode.valueOf(params.get(ConsoleAndFileDriverConfiguration.MODE_ARG));
        }

        // Validation mode does not require parameter directory
        if (operationMode != OperationMode.VALIDATE_DATABASE) {
            compulsoryKeys.add(LdbcFinBenchTransactionWorkloadConfiguration.PARAMETERS_DIRECTORY);
        }

        if (params.containsKey(LdbcFinBenchTransactionWorkloadConfiguration.BATCH_SIZE)) {
            batchSize = Double.parseDouble(params.get(LdbcFinBenchTransactionWorkloadConfiguration.BATCH_SIZE));
        } else {
            batchSize = LdbcFinBenchTransactionWorkloadConfiguration.DEFAULT_BATCH_SIZE;
        }

        compulsoryKeys.addAll(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_ENABLE_KEYS);
        compulsoryKeys.addAll(LdbcFinBenchTransactionWorkloadConfiguration.WRITE_OPERATION_ENABLE_KEYS);
        compulsoryKeys.addAll(LdbcFinBenchTransactionWorkloadConfiguration.SIMPLE_READ_OPERATION_ENABLE_KEYS);

        Set<String> missingPropertyParameters =
            LdbcFinBenchTransactionWorkloadConfiguration.missingParameters(params, compulsoryKeys);
        if (!missingPropertyParameters.isEmpty()) {
            throw new WorkloadException(format("Workload could not initialize due to missing parameters: %s",
                missingPropertyParameters.toString()));
        }

        if (params.containsKey(LdbcFinBenchTransactionWorkloadConfiguration.UPDATES_DIRECTORY)) {
            updatesDir = new File(params.get(LdbcFinBenchTransactionWorkloadConfiguration.UPDATES_DIRECTORY).trim());
            if (!updatesDir.exists()) {
                throw new WorkloadException(
                    format("Updates directory does not exist%nDirectory: %s",
                        updatesDir.getAbsolutePath()));
            }
            if (!updatesDir.isDirectory()) {
                throw new WorkloadException(
                    format("Updates directory is not a directory%nDirectory: %s",
                        updatesDir.getAbsolutePath()));
            }
        }

        if (operationMode != OperationMode.VALIDATE_DATABASE) {
            if (params.containsKey(LdbcFinBenchTransactionWorkloadConfiguration.FILES_SUFFIX)) {
                fileSuffix = params.get(LdbcFinBenchTransactionWorkloadConfiguration.FILES_SUFFIX).trim();
            } else {
                fileSuffix = LdbcFinBenchTransactionWorkloadConfiguration.DEFAULT_FILE_SUFFIX;
            }
            parametersDir =
                new File(params.get(LdbcFinBenchTransactionWorkloadConfiguration.PARAMETERS_DIRECTORY).trim());
            if (!parametersDir.exists()) {
                throw new WorkloadException(
                    format("Parameters directory does not exist: %s", parametersDir.getAbsolutePath()));
            }
            for (String readOperationParamsFilename :
                LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_PARAMS_FILENAMES.values()) {
                File readOperationParamsFile = new File(parametersDir, readOperationParamsFilename
                        + LdbcFinBenchTransactionWorkloadConfiguration.FILE_SEPARATOR + fileSuffix);
                if (!readOperationParamsFile.exists()) {
                    throw new WorkloadException(
                        format("Read operation parameters file does not exist: %s",
                            readOperationParamsFile.getAbsolutePath()));
                }
            }
        }

        enabledLongReadOperationTypes =
            getEnabledOperationsHashset(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_ENABLE_KEYS,
                params);
        enabledSimpleReadOperationTypes =
            getEnabledOperationsHashset(LdbcFinBenchTransactionWorkloadConfiguration.SIMPLE_READ_OPERATION_ENABLE_KEYS,
                params);
        if (!enabledSimpleReadOperationTypes.isEmpty()) {
            if (!params.containsKey(LdbcFinBenchTransactionWorkloadConfiguration.simple_read_dissipation)) {
                throw new WorkloadException(format("Configuration parameter missing: %s",
                    LdbcFinBenchTransactionWorkloadConfiguration.simple_read_dissipation));
            }
            simpleReadDissipationFactor = Double.parseDouble(
                params.get(LdbcFinBenchTransactionWorkloadConfiguration.simple_read_dissipation).trim()
            );
            if (simpleReadDissipationFactor < 0 || simpleReadDissipationFactor > 1) {
                throw new WorkloadException(
                    format("Configuration parameter %s should be in interval [1.0,0.0] but is: %s",
                        LdbcFinBenchTransactionWorkloadConfiguration.simple_read_dissipation,
                        simpleReadDissipationFactor));
            }
        }

        enabledWriteOperationTypes =
            getEnabledOperationsHashset(LdbcFinBenchTransactionWorkloadConfiguration.WRITE_OPERATION_ENABLE_KEYS,
                params);
        //enabledDeleteOperationTypes = getEnabledOperationsHashset
        // (LdbcFinBenchTransactionWorkloadConfiguration.DELETE_OPERATION_ENABLE_KEYS, params);
        enabledUpdateOperationTypes = new HashSet<Class<? extends Operation>>(enabledWriteOperationTypes);
        // enabledUpdateOperationTypes.addAll(enabledDeleteOperationTypes);
        // First load the scale factor from the provided properties file, then load the frequency keys from resources
        if (!params.containsKey(LdbcFinBenchTransactionWorkloadConfiguration.SCALE_FACTOR)) {
            // if SCALE_FACTOR is missing but writes are enabled it is an error
            throw new WorkloadException(
                format("Workload could not initialize. Missing parameter: %s",
                    LdbcFinBenchTransactionWorkloadConfiguration.SCALE_FACTOR));
        }

        String scaleFactor = params.get(LdbcFinBenchTransactionWorkloadConfiguration.SCALE_FACTOR).trim();
        // Load the frequencyKeys for the appropiate scale factor if that scale factor is supported

        String scaleFactorPropertiesPath = "configuration/ldbc/finbench/transaction/sf" + scaleFactor + ".properties";
        // Load the properties file, throw error if file is not present (and thus not supported)
        final Properties scaleFactorProperties = new Properties();

        try (final InputStream stream =
                 this.getClass().getClassLoader().getResourceAsStream(scaleFactorPropertiesPath)) {
            scaleFactorProperties.load(stream);
        } catch (IOException e) {
            throw new WorkloadException(
                format("Workload could not initialize. Scale factor %s not supported. %s",
                    scaleFactor, e));
        }

        Map<String, String> tempFileParams = MapUtils.propertiesToMap(scaleFactorProperties);
        Map<String, String> tmp = new HashMap<String, String>(tempFileParams);

        // Check if validation params creation is used. If so, set the frequencies to 1
        if (OperationMode.valueOf(params.get(ConsoleAndFileDriverConfiguration.MODE_ARG))
            == OperationMode.CREATE_VALIDATION) {
            Map<String, String> freqs = new HashMap<String, String>();
            String updateInterleave = tmp.get(LdbcFinBenchTransactionWorkloadConfiguration.UPDATE_INTERLEAVE);
            freqs.put(LdbcFinBenchTransactionWorkloadConfiguration.UPDATE_INTERLEAVE, updateInterleave);
            for (String operationFrequencyKey :
                LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_FREQUENCY_KEYS) {
                freqs.put(operationFrequencyKey, "1");
            }
            freqs.keySet().removeAll(params.keySet());
            params.putAll(freqs);
        } else {
            tmp.keySet().removeAll(params.keySet());
            params.putAll(tmp);
        }

        List<String> frequencyKeys =
            Lists.newArrayList(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_FREQUENCY_KEYS);
        Set<String> missingFrequencyKeys = LdbcFinBenchTransactionWorkloadConfiguration
            .missingParameters(params, frequencyKeys);

        if (enabledUpdateOperationTypes.isEmpty()
            && !params.containsKey(LdbcFinBenchTransactionWorkloadConfiguration.UPDATE_INTERLEAVE)) {
            // if UPDATE_INTERLEAVE is missing and writes are disabled set it to DEFAULT
            params.put(
                LdbcFinBenchTransactionWorkloadConfiguration.UPDATE_INTERLEAVE,
                LdbcFinBenchTransactionWorkloadConfiguration.DEFAULT_UPDATE_INTERLEAVE
            );
        }
        if (!params.containsKey(LdbcFinBenchTransactionWorkloadConfiguration.UPDATE_INTERLEAVE)) {
            // if UPDATE_INTERLEAVE is missing but writes are enabled it is an error
            throw new WorkloadException(
                format("Workload could not initialize. Missing parameter: %s",
                    LdbcFinBenchTransactionWorkloadConfiguration.UPDATE_INTERLEAVE));
        }
        updateInterleaveAsMilli =
            Integer.parseInt(params.get(LdbcFinBenchTransactionWorkloadConfiguration.UPDATE_INTERLEAVE).trim());

        if (missingFrequencyKeys.isEmpty()) {
            // all frequency arguments were given, compute interleave based on frequencies
            params = LdbcFinBenchTransactionWorkloadConfiguration.convertFrequenciesToInterleaves(params);
        } else {
            // if any frequencies are not set, there should be specified interleave times for read queries
            Set<String> missingInterleaveKeys = LdbcFinBenchTransactionWorkloadConfiguration.missingParameters(
                params,
                LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_INTERLEAVE_KEYS
            );
            if (!missingInterleaveKeys.isEmpty()) {
                throw new WorkloadException(format(
                    "Workload could not initialize. One of the following groups of parameters should be set: %s "
                        + "or %s",
                    missingFrequencyKeys.toString(), missingInterleaveKeys.toString()));
            }
        }
        try {
            longReadInterleavesAsMilli = new HashMap<>();
            longReadInterleavesAsMilli.put(ComplexRead1.TYPE, Long.parseLong(
                params.get(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_1_INTERLEAVE_KEY)
                    .trim()));
            longReadInterleavesAsMilli.put(ComplexRead2.TYPE, Long.parseLong(
                params.get(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_2_INTERLEAVE_KEY)
                    .trim()));
            longReadInterleavesAsMilli.put(ComplexRead3.TYPE, Long.parseLong(
                params.get(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_3_INTERLEAVE_KEY)
                    .trim()));
            longReadInterleavesAsMilli.put(ComplexRead4.TYPE, Long.parseLong(
                params.get(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_4_INTERLEAVE_KEY)
                    .trim()));
            longReadInterleavesAsMilli.put(ComplexRead5.TYPE, Long.parseLong(
                params.get(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_5_INTERLEAVE_KEY)
                    .trim()));
            longReadInterleavesAsMilli.put(ComplexRead6.TYPE, Long.parseLong(
                params.get(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_6_INTERLEAVE_KEY)
                    .trim()));
            longReadInterleavesAsMilli.put(ComplexRead7.TYPE, Long.parseLong(
                params.get(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_7_INTERLEAVE_KEY)
                    .trim()));
            longReadInterleavesAsMilli.put(ComplexRead8.TYPE, Long.parseLong(
                params.get(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_8_INTERLEAVE_KEY)
                    .trim()));
            longReadInterleavesAsMilli.put(ComplexRead9.TYPE, Long.parseLong(
                params.get(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_9_INTERLEAVE_KEY)
                    .trim()));
            longReadInterleavesAsMilli.put(ComplexRead10.TYPE, Long.parseLong(
                params.get(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_10_INTERLEAVE_KEY)
                    .trim()));
            longReadInterleavesAsMilli.put(ComplexRead11.TYPE, Long.parseLong(
                params.get(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_11_INTERLEAVE_KEY)
                    .trim()));
            longReadInterleavesAsMilli.put(ComplexRead12.TYPE, Long.parseLong(
                params.get(LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_12_INTERLEAVE_KEY)
                    .trim()));

        } catch (NumberFormatException e) {
            throw new WorkloadException("Unable to parse one of the read operation interleave values", e);
        }

        this.compressionRatio = Double.parseDouble(
            params.get(ConsoleAndFileDriverConfiguration.TIME_COMPRESSION_RATIO_ARG).trim()
        );
    }

    /**
     * Create set with enabled operation keys
     *
     * @param enabledOperationKeys
     * @param params
     * @return
     */
    private Set<Class<? extends Operation>> getEnabledOperationsHashset(List<String> enabledOperationKeys,
                                                                        Map<String, String> params)
        throws WorkloadException {
        Set<Class<? extends Operation>> enabledOperationTypes = new HashSet<>();
        for (String operationEnableKey : enabledOperationKeys) {
            String operationEnabledString = params.get(operationEnableKey).trim();
            Boolean operationEnabled = Boolean.parseBoolean(operationEnabledString);
            String operationClassName =
                LdbcFinBenchTransactionWorkloadConfiguration.LDBC_FINBENCH_TRANSACTION_PACKAGE_PREFIX
                    + LdbcFinBenchTransactionWorkloadConfiguration.removePrefix(
                    LdbcFinBenchTransactionWorkloadConfiguration.removeSuffix(
                        operationEnableKey,
                        LdbcFinBenchTransactionWorkloadConfiguration.ENABLE_SUFFIX
                    ),
                    LdbcFinBenchTransactionWorkloadConfiguration
                        .LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX
                );
            try {
                Class operationClass = ClassLoaderHelper.loadClass(operationClassName);
                if (operationEnabled) {
                    enabledOperationTypes.add(operationClass);
                }

            } catch (ClassLoadingException e) {
                throw new WorkloadException(
                    format(
                        "Unable to load operation class for parameter: %s%nGuessed incorrect class name: %s",
                        operationEnableKey, operationClassName),
                    e
                );
            }
        }
        return enabledOperationTypes;
    }

    @Override
    public Class<? extends Operation> getOperationClass() {
        return LdbcOperation.class;
    }

    @Override
    protected void onClose() throws IOException {
        if (runnableBatchLoader != null && !runnableBatchLoader.isInterrupted()) {
            runnableBatchLoader.interrupt();
        }
    }

    /**
     * Peek the first operation and fetch the operation start time.
     *
     * @param updateStream             The Iterator with update operations
     * @param workloadStartTimeAsMilli The initial start time as milli
     * @return New workload start time as milli
     * @throws WorkloadException
     */
    private long getOperationStreamStartTime(
        Iterator<Operation> updateStream,
        long workloadStartTimeAsMilli
    ) throws WorkloadException {
        PeekingIterator<Operation> unfilteredUpdateOperations = Iterators.peekingIterator(updateStream);
        try {
            if (unfilteredUpdateOperations.hasNext()
                && unfilteredUpdateOperations.peek().scheduledStartTimeAsMilli() < workloadStartTimeAsMilli) {
                workloadStartTimeAsMilli = unfilteredUpdateOperations.peek().scheduledStartTimeAsMilli();
            }
        } catch (NoSuchElementException e) {
            // do nothing, exception just means that stream was empty
        }
        return workloadStartTimeAsMilli;
    }


    /**
     * Get the operation streams (substitution parameters)
     *
     * @param gf                       Generator factory to use
     * @param workloadStartTimeAsMilli The workloadStartTimeAsMilli
     * @param loader                   Loader to open the csv files
     * @return List of operation streams
     * @throws WorkloadException
     */
    private List<Iterator<?>> getOperationStreams(
        GeneratorFactory gf,
        long workloadStartTimeAsMilli,
        FileLoader loader
    ) throws WorkloadException {
        List<Iterator<?>> asynchronousNonDependencyStreamsList = new ArrayList<>();
        /*
         * Create read operation streams, with specified interleaves
         */
        OperationStreamReader readOperationStream = new OperationStreamReader(loader);
        Map<Integer, EventStreamReader.EventDecoder<Operation>> decoders = QueryEventStreamReader.getDecoders();
        Map<Class<? extends Operation>, Integer> classToTypeMap = MapUtils.invertMap(operationTypeToClassMapping());
        for (Class enabledClass : enabledLongReadOperationTypes) {
            Integer type = classToTypeMap.get(enabledClass);
            Iterator<Operation> eventOperationStream = readOperationStream.readOperationStream(
                decoders.get(type),
                new File(parametersDir,
                    LdbcFinBenchTransactionWorkloadConfiguration.COMPLEX_READ_OPERATION_PARAMS_FILENAMES.get(type)
                        + LdbcFinBenchTransactionWorkloadConfiguration.FILE_SEPARATOR + fileSuffix)
            );
            long readOperationInterleaveAsMilli = longReadInterleavesAsMilli.get(type);
            Iterator<Long> operationStartTimes =
                gf.incrementing(workloadStartTimeAsMilli + readOperationInterleaveAsMilli,
                    readOperationInterleaveAsMilli);

            Iterator<Operation> operationStream = gf.assignStartTimes(
                operationStartTimes,
                new QueryEventStreamReader(gf.repeating(eventOperationStream))
            );
            asynchronousNonDependencyStreamsList.add(operationStream);
        }
        return asynchronousNonDependencyStreamsList;
    }

    /**
     * Create Simple read operations
     *
     * @param hasDbConnected Whether a database is connected
     * @return
     */
    private LdbcFinBenchSimpleReadGenerator getSimpleReadGenerator(boolean hasDbConnected) {
        RandomDataGeneratorFactory randomFactory = new RandomDataGeneratorFactory(42L);
        double initialProbability = 1.0;

        Queue<Tuple4<Long, Double, Date, Date>> accountIdBuffer;
        Queue<Long> personIdBuffer;
        Queue<Long> companyIdBuffer;
        LdbcFinBenchSimpleReadGenerator.ScheduledStartTimePolicy scheduledStartTimePolicy;
        LdbcFinBenchSimpleReadGenerator.BufferReplenishFun bufferReplenishFun;
        if (hasDbConnected) {
            accountIdBuffer = LdbcFinBenchSimpleReadGenerator.synchronizedCircularTuple4QueueBuffer(1024);
            personIdBuffer = LdbcFinBenchSimpleReadGenerator.synchronizedCircularQueueBuffer(1024);
            companyIdBuffer = LdbcFinBenchSimpleReadGenerator.synchronizedCircularQueueBuffer(1024);
            scheduledStartTimePolicy =
                LdbcFinBenchSimpleReadGenerator.ScheduledStartTimePolicy.PREVIOUS_OPERATION_ACTUAL_FINISH_TIME;
            bufferReplenishFun =
                new LdbcFinBenchSimpleReadGenerator.ResultBufferReplenishFun(accountIdBuffer, personIdBuffer,
                    companyIdBuffer);
        } else {
            accountIdBuffer = LdbcFinBenchSimpleReadGenerator.constantTuple4Buffer(
                new Tuple4<>(1L, 1D, new Date(), new Date()));
            personIdBuffer = LdbcFinBenchSimpleReadGenerator.constantBuffer(1);
            companyIdBuffer = LdbcFinBenchSimpleReadGenerator.constantBuffer(1);
            scheduledStartTimePolicy =
                LdbcFinBenchSimpleReadGenerator.ScheduledStartTimePolicy.PREVIOUS_OPERATION_SCHEDULED_START_TIME;
            bufferReplenishFun = new LdbcFinBenchSimpleReadGenerator.NoOpBufferReplenishFun();
        }
        return new LdbcFinBenchSimpleReadGenerator(
            initialProbability,
            simpleReadDissipationFactor,
            updateInterleaveAsMilli,
            enabledSimpleReadOperationTypes,
            compressionRatio,
            accountIdBuffer,
            personIdBuffer,
            companyIdBuffer,
            randomFactory,
            longReadInterleavesAsMilli,
            scheduledStartTimePolicy,
            bufferReplenishFun
        );
    }


    @Override
    protected WorkloadStreams getStreams(GeneratorFactory gf, boolean hasDbConnected) throws WorkloadException {

        long workloadStartTimeAsMilli = Long.MAX_VALUE;
        WorkloadStreams ldbcFinbenchWorkloadStreams = new WorkloadStreams();
        Iterator<Operation> asynchronousDependencyStreams;
        List<Iterator<?>> asynchronousNonDependencyStreamsList;
        Set<Class<? extends Operation>> dependentAsynchronousOperationTypes = Sets.newHashSet();
        Set<Class<? extends Operation>> dependencyAsynchronousOperationTypes = Sets.newHashSet();

        dependencyAsynchronousOperationTypes.addAll(enabledUpdateOperationTypes);
        // dependentAsynchronousOperationTypes.addAll(enabledLongReadOperationTypes);

        FileLoader loader;
        try {
            DuckDbExtractor db = new DuckDbExtractor();
            loader = new FileLoader(db);
        } catch (SQLException e) {
            throw new WorkloadException(format("Error creating loader for operation streams %s", e));
        }

        FileLoader updateLoader;
        try {
            DuckDbExtractor db = new DuckDbExtractor();
            updateLoader = new FileLoader(db);
        } catch (SQLException e) {
            throw new WorkloadException(format("Error creating updateLoader for operation streams %s", e));
        }


        /*
         * WRITES
         */
        if (!enabledUpdateOperationTypes.isEmpty()) {
            asynchronousDependencyStreams = setBatchedUpdateStreams(gf, workloadStartTimeAsMilli, updateLoader);
            workloadStartTimeAsMilli =
                getOperationStreamStartTime(asynchronousDependencyStreams, workloadStartTimeAsMilli);
        } else {
            asynchronousDependencyStreams = Collections.emptyIterator();
        }

        if (Long.MAX_VALUE == workloadStartTimeAsMilli) {
            workloadStartTimeAsMilli = 0;
        }

        /*
         * LONG READS
         */
        asynchronousNonDependencyStreamsList = getOperationStreams(gf, workloadStartTimeAsMilli, loader);

        /*
         * Merge all non dependency asynchronous operation streams, ordered by operation start times
         */
        Iterator<Operation> asynchronousNonDependencyStreams = gf.mergeSortOperationsByTimeStamp(
            asynchronousNonDependencyStreamsList.toArray(
                new Iterator[asynchronousNonDependencyStreamsList.size()]
            )
        );

        /*
         * SIMPLE READS
         */
        ChildOperationGenerator simpleReadsChildGenerator = null;
        if (!enabledSimpleReadOperationTypes.isEmpty()) {
            simpleReadsChildGenerator = getSimpleReadGenerator(hasDbConnected);
        }

        /*
         * FINAL STREAMS
         */
        ldbcFinbenchWorkloadStreams.setAsynchronousStream(
            dependentAsynchronousOperationTypes,
            dependencyAsynchronousOperationTypes,
            asynchronousDependencyStreams,
            asynchronousNonDependencyStreams,
            simpleReadsChildGenerator
        );

        return ldbcFinbenchWorkloadStreams;
    }

    @Override
    public Set<Class> enabledValidationOperations() {
        Set<Class> enabledOperations = new HashSet<>();
        enabledOperations.addAll(enabledLongReadOperationTypes);
        enabledOperations.addAll(enabledUpdateOperationTypes);
        enabledOperations.addAll(enabledSimpleReadOperationTypes);
        return enabledOperations;
    }

    private Iterator<Operation> setBatchedUpdateStreams(
        GeneratorFactory gf,
        long workloadStartTimeAsMilli,
        FileLoader loader
    ) throws WorkloadException {
        long batchSizeInMillis = Math.round(TimeUnit.HOURS.toMillis(1) * batchSize);

        Set<Class<? extends Operation>> dependencyUpdateOperationTypes = Sets.<Class<? extends Operation>>newHashSet();

        for (Class class1 : enabledUpdateOperationTypes) {
            dependencyUpdateOperationTypes.add(class1);
        }

        int batchQueueSize = LdbcFinBenchTransactionWorkloadConfiguration.BUFFERED_QUEUE_SIZE;

        BlockingQueue<Iterator<Operation>> blockingQueue = new LinkedBlockingQueue<>(batchQueueSize);
        CountDownLatch finishInit = new CountDownLatch(1);
        runnableBatchLoader = new RunnableOperationStreamBatchLoader(
            loader,
            gf,
            updatesDir,
            fileSuffix,
            blockingQueue,
            dependencyUpdateOperationTypes,
            batchSizeInMillis,
            finishInit
        );
        runnableBatchLoader.start();
        try {
            finishInit.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        OperationStreamBuffer buffer = new OperationStreamBuffer(blockingQueue);
        BufferedIterator bufferedIterator = new BufferedIterator(buffer);
        bufferedIterator.init();
        return bufferedIterator;
    }

    /**
     * Creates the validation parameter filter, which determines the amount of validation parameters
     *
     * @param requiredValidationParameterCount The total validation parameters to create
     */
    @Override
    public DbValidationParametersFilter dbValidationParametersFilter(Integer requiredValidationParameterCount) {
        Integer operationTypeCount = enabledLongReadOperationTypes.size() + enabledUpdateOperationTypes.size();

        // Calculate amount of validation operations to create
        long minimumResultCountPerOperationType = Math.max(
            1,
            Math.round(Math.floor(
                requiredValidationParameterCount.doubleValue() / operationTypeCount.doubleValue()))
        );

        final Map<Class, Long> remainingRequiredResultsPerType = new HashMap<>();
        for (Class updateOperationType : enabledUpdateOperationTypes) {
            remainingRequiredResultsPerType.put(updateOperationType, minimumResultCountPerOperationType);
        }
        for (Class longReadOperationType : enabledLongReadOperationTypes) {
            remainingRequiredResultsPerType.put(longReadOperationType, minimumResultCountPerOperationType);
        }

        for (Class simpleReadOperationType : enabledSimpleReadOperationTypes) {
            remainingRequiredResultsPerType.put(simpleReadOperationType, minimumResultCountPerOperationType);
        }

        return new LdbcFinBenchTransactionDbValidationParametersFilter(
            remainingRequiredResultsPerType,
            // Writes are required to determine short reads operations to inject
            enabledSimpleReadOperationTypes
        );
    }

    @Override
    public long maxExpectedInterleaveAsMilli() {
        return TimeUnit.HOURS.toMillis(1);
    }
}
