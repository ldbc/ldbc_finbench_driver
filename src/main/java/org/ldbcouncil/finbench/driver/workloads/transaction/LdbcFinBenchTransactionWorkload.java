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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.ldbcouncil.finbench.driver.ChildOperationGenerator;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.WorkloadStreams;
import org.ldbcouncil.finbench.driver.control.ConsoleAndFileDriverConfiguration;
import org.ldbcouncil.finbench.driver.control.OperationMode;
import org.ldbcouncil.finbench.driver.csv.DuckDbParquetExtractor;
import org.ldbcouncil.finbench.driver.csv.ParquetLoader;
import org.ldbcouncil.finbench.driver.generator.BufferedIterator;
import org.ldbcouncil.finbench.driver.generator.EventStreamReader;
import org.ldbcouncil.finbench.driver.generator.GeneratorFactory;
import org.ldbcouncil.finbench.driver.generator.OperationStreamBuffer;
import org.ldbcouncil.finbench.driver.generator.RandomDataGeneratorFactory;
import org.ldbcouncil.finbench.driver.util.ClassLoaderHelper;
import org.ldbcouncil.finbench.driver.util.ClassLoadingException;
import org.ldbcouncil.finbench.driver.util.MapUtils;

// TODO: implement this
public class LdbcFinBenchTransactionWorkload extends Workload {
    private Map<Integer,Long> longReadInterleavesAsMilli;
    private File parametersDir;
    private File updatesDir;
    private long updateInterleaveAsMilli;
    private double compressionRatio;
    private double shortReadDissipationFactor;
    private OperationMode operationMode;
    private double batchSize;
    private Set<Class<? extends Operation>> enabledLongReadOperationTypes;
    private Set<Class<? extends Operation>> enabledShortReadOperationTypes;
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
        if (params.containsKey(ConsoleAndFileDriverConfiguration.MODE_ARG)){
            operationMode = OperationMode.valueOf(params.get(ConsoleAndFileDriverConfiguration.MODE_ARG));
        }

        // Validation mode does not require parameter directory
        if (operationMode != OperationMode.VALIDATE_DATABASE )
        {
            compulsoryKeys.add( LdbcFinBenchTransactionWorkloadConfiguration.PARAMETERS_DIRECTORY );
        }

        if (params.containsKey(LdbcFinBenchTransactionWorkloadConfiguration.BATCH_SIZE)){
            batchSize = Double.parseDouble(params.get(LdbcFinBenchTransactionWorkloadConfiguration.BATCH_SIZE));
        }
        else
        {
            batchSize = LdbcFinBenchTransactionWorkloadConfiguration.DEFAULT_BATCH_SIZE;
        }

        compulsoryKeys.addAll( LdbcFinBenchTransactionWorkloadConfiguration.LONG_READ_OPERATION_ENABLE_KEYS );
        compulsoryKeys.addAll( LdbcFinBenchTransactionWorkloadConfiguration.WRITE_OPERATION_ENABLE_KEYS );
        compulsoryKeys.addAll( LdbcFinBenchTransactionWorkloadConfiguration.SHORT_READ_OPERATION_ENABLE_KEYS );

        Set<String> missingPropertyParameters =
            LdbcFinBenchTransactionWorkloadConfiguration.missingParameters( params, compulsoryKeys );
        if ( !missingPropertyParameters.isEmpty() )
        {
            throw new WorkloadException( format( "Workload could not initialize due to missing parameters: %s",
                missingPropertyParameters.toString() ) );
        }

        if ( params.containsKey( LdbcFinBenchTransactionWorkloadConfiguration.UPDATES_DIRECTORY ) )
        {
            updatesDir = new File( params.get( LdbcFinBenchTransactionWorkloadConfiguration.UPDATES_DIRECTORY ).trim() );
            if ( !updatesDir.exists() )
            {
                throw new WorkloadException(
                    format( "Updates directory does not exist%nDirectory: %s",
                        updatesDir.getAbsolutePath() ) );
            }
            if ( !updatesDir.isDirectory() )
            {
                throw new WorkloadException(
                    format( "Updates directory is not a directory%nDirectory: %s",
                        updatesDir.getAbsolutePath() ) );
            }
        }

        if (operationMode != OperationMode.VALIDATE_DATABASE)
        {
            parametersDir = new File( params.get( LdbcFinBenchTransactionWorkloadConfiguration.PARAMETERS_DIRECTORY ).trim() );
            if ( !parametersDir.exists() )
            {
                throw new WorkloadException(
                    format( "Parameters directory does not exist: %s", parametersDir.getAbsolutePath() ) );
            }
           /* for ( String readOperationParamsFilename :
                LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_PARAMS_FILENAMES.values() )
            {
                File readOperationParamsFile = new File( parametersDir, readOperationParamsFilename );
                if ( !readOperationParamsFile.exists() )
                {
                    throw new WorkloadException(
                        format( "Read operation parameters file does not exist: %s",
                            readOperationParamsFile.getAbsolutePath() ) );
                }
            }*/
        }

        enabledLongReadOperationTypes = getEnabledOperationsHashset(LdbcFinBenchTransactionWorkloadConfiguration.LONG_READ_OPERATION_ENABLE_KEYS, params);
        enabledShortReadOperationTypes = getEnabledOperationsHashset(LdbcFinBenchTransactionWorkloadConfiguration.SHORT_READ_OPERATION_ENABLE_KEYS, params);
        if ( !enabledShortReadOperationTypes.isEmpty() )
        {
            if ( !params.containsKey( LdbcFinBenchTransactionWorkloadConfiguration.SHORT_READ_DISSIPATION ) )
            {
                throw new WorkloadException( format( "Configuration parameter missing: %s",
                    LdbcFinBenchTransactionWorkloadConfiguration.SHORT_READ_DISSIPATION ) );
            }
            shortReadDissipationFactor = Double.parseDouble(
                params.get( LdbcFinBenchTransactionWorkloadConfiguration.SHORT_READ_DISSIPATION ).trim()
            );
            if ( shortReadDissipationFactor < 0 || shortReadDissipationFactor > 1 )
            {
                throw new WorkloadException(
                    format( "Configuration parameter %s should be in interval [1.0,0.0] but is: %s",
                        LdbcFinBenchTransactionWorkloadConfiguration.SHORT_READ_DISSIPATION , shortReadDissipationFactor ) );
            }
        }

        enabledWriteOperationTypes = getEnabledOperationsHashset(LdbcFinBenchTransactionWorkloadConfiguration.WRITE_OPERATION_ENABLE_KEYS, params);
        //enabledDeleteOperationTypes = getEnabledOperationsHashset(LdbcFinBenchTransactionWorkloadConfiguration.DELETE_OPERATION_ENABLE_KEYS, params);
        enabledUpdateOperationTypes = new HashSet<Class<? extends Operation>>(enabledWriteOperationTypes);
        // enabledUpdateOperationTypes.addAll(enabledDeleteOperationTypes);
        // First load the scale factor from the provided properties file, then load the frequency keys from resources
        if (!params.containsKey(LdbcFinBenchTransactionWorkloadConfiguration.SCALE_FACTOR))
        {
            // if SCALE_FACTOR is missing but writes are enabled it is an error
            throw new WorkloadException(
                format( "Workload could not initialize. Missing parameter: %s",
                    LdbcFinBenchTransactionWorkloadConfiguration.SCALE_FACTOR ) );
        }

        String scaleFactor = params.get( LdbcFinBenchTransactionWorkloadConfiguration.SCALE_FACTOR ).trim();
        // Load the frequencyKeys for the appropiate scale factor if that scale factor is supported

        String scaleFactorPropertiesPath = "configuration/ldbc/finbench/transaction/sf" + scaleFactor  + ".properties";
        // Load the properties file, throw error if file is not present (and thus not supported)
        final Properties scaleFactorProperties = new Properties();

        try (final InputStream stream =
                 this.getClass().getClassLoader().getResourceAsStream(scaleFactorPropertiesPath)) {
            scaleFactorProperties.load(stream);
        }
        catch (IOException e){
            throw new WorkloadException(
                format( "Workload could not initialize. Scale factor %s not supported. %s",
                    scaleFactor, e));
        }

        Map<String,String> tempFileParams = MapUtils.propertiesToMap( scaleFactorProperties );
        Map<String,String> tmp = new HashMap<String,String>(tempFileParams);

        // Check if validation params creation is used. If so, set the frequencies to 1
        if ( OperationMode.valueOf(params.get(ConsoleAndFileDriverConfiguration.MODE_ARG)) == OperationMode.CREATE_VALIDATION )
        {
            Map<String, String> freqs = new HashMap<String, String>();
            String updateInterleave = tmp.get(LdbcFinBenchTransactionWorkloadConfiguration.UPDATE_INTERLEAVE);
            freqs.put(LdbcFinBenchTransactionWorkloadConfiguration.UPDATE_INTERLEAVE, updateInterleave);
            for (String operationFrequencyKey : LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_FREQUENCY_KEYS) {
                freqs.put(operationFrequencyKey, "1");
            }
            freqs.keySet().removeAll(params.keySet());
            params.putAll(freqs);
        }
        else
        {
            tmp.keySet().removeAll(params.keySet());
            params.putAll(tmp);
        }

        List<String> frequencyKeys =
            Lists.newArrayList( LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_FREQUENCY_KEYS );
        Set<String> missingFrequencyKeys = LdbcFinBenchTransactionWorkloadConfiguration
            .missingParameters( params, frequencyKeys );

        if ( enabledUpdateOperationTypes.isEmpty() &&
            !params.containsKey( LdbcFinBenchTransactionWorkloadConfiguration.UPDATE_INTERLEAVE ) )
        {
            // if UPDATE_INTERLEAVE is missing and writes are disabled set it to DEFAULT
            params.put(
                LdbcFinBenchTransactionWorkloadConfiguration.UPDATE_INTERLEAVE,
                LdbcFinBenchTransactionWorkloadConfiguration.DEFAULT_UPDATE_INTERLEAVE
            );
        }
        if ( !params.containsKey( LdbcFinBenchTransactionWorkloadConfiguration.UPDATE_INTERLEAVE ) )
        {
            // if UPDATE_INTERLEAVE is missing but writes are enabled it is an error
            throw new WorkloadException(
                format( "Workload could not initialize. Missing parameter: %s",
                    LdbcFinBenchTransactionWorkloadConfiguration.UPDATE_INTERLEAVE ) );
        }
        updateInterleaveAsMilli =
            Integer.parseInt( params.get( LdbcFinBenchTransactionWorkloadConfiguration.UPDATE_INTERLEAVE ).trim() );

        if ( missingFrequencyKeys.isEmpty() )
        {
            // all frequency arguments were given, compute interleave based on frequencies
            params = LdbcFinBenchTransactionWorkloadConfiguration.convertFrequenciesToInterleaves( params );
        }
        else
        {
            // if any frequencies are not set, there should be specified interleave times for read queries
            Set<String> missingInterleaveKeys = LdbcFinBenchTransactionWorkloadConfiguration.missingParameters(
                params,
                LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_INTERLEAVE_KEYS
            );
            if ( !missingInterleaveKeys.isEmpty() )
            {
                throw new WorkloadException( format(
                    "Workload could not initialize. One of the following groups of parameters should be set: %s " +
                        "or %s",
                    missingFrequencyKeys.toString(), missingInterleaveKeys.toString() ) );
            }
        }
        try
        {
            longReadInterleavesAsMilli = new HashMap<>();
            longReadInterleavesAsMilli.put( ComplexRead1.TYPE, Long.parseLong(params.get( LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_1_INTERLEAVE_KEY ).trim() ) );
            longReadInterleavesAsMilli.put( ComplexRead2.TYPE, Long.parseLong(params.get( LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_2_INTERLEAVE_KEY ).trim() ) );
            longReadInterleavesAsMilli.put( ComplexRead3.TYPE, Long.parseLong(params.get( LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_3_INTERLEAVE_KEY ).trim() ) );
            longReadInterleavesAsMilli.put( ComplexRead4.TYPE, Long.parseLong(params.get( LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_4_INTERLEAVE_KEY ).trim() ) );
            longReadInterleavesAsMilli.put( ComplexRead5.TYPE, Long.parseLong(params.get( LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_5_INTERLEAVE_KEY ).trim() ) );
            longReadInterleavesAsMilli.put( ComplexRead6.TYPE, Long.parseLong(params.get( LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_6_INTERLEAVE_KEY ).trim() ) );
            longReadInterleavesAsMilli.put( ComplexRead7.TYPE, Long.parseLong(params.get( LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_7_INTERLEAVE_KEY ).trim() ) );
            longReadInterleavesAsMilli.put( ComplexRead8.TYPE, Long.parseLong(params.get( LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_8_INTERLEAVE_KEY ).trim() ) );
            longReadInterleavesAsMilli.put( ComplexRead9.TYPE, Long.parseLong(params.get( LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_9_INTERLEAVE_KEY ).trim() ) );
            longReadInterleavesAsMilli.put( ComplexRead10.TYPE, Long.parseLong(params.get( LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_10_INTERLEAVE_KEY ).trim() ) );
            longReadInterleavesAsMilli.put( ComplexRead11.TYPE, Long.parseLong(params.get( LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_11_INTERLEAVE_KEY ).trim() ) );
            longReadInterleavesAsMilli.put( ComplexRead12.TYPE, Long.parseLong(params.get( LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_12_INTERLEAVE_KEY ).trim() ) );
            longReadInterleavesAsMilli.put( ComplexRead13.TYPE, Long.parseLong(params.get( LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_13_INTERLEAVE_KEY ).trim() ) );

        }
        catch ( NumberFormatException e )
        {
            throw new WorkloadException( "Unable to parse one of the read operation interleave values", e );
        }

        this.compressionRatio = Double.parseDouble(
            params.get( ConsoleAndFileDriverConfiguration.TIME_COMPRESSION_RATIO_ARG ).trim()
        );
    }

    /**
     * Create set with enabled operation keys
     * @param enabledOperationKeys
     * @param params
     * @return
     */
    private Set<Class<? extends Operation>> getEnabledOperationsHashset(List<String> enabledOperationKeys, Map<String,String> params) throws WorkloadException
    {
        Set<Class<? extends Operation>> enabledOperationTypes = new HashSet<>();
        for ( String operationEnableKey : enabledOperationKeys )
        {
            String operationEnabledString = params.get( operationEnableKey ).trim();
            Boolean operationEnabled = Boolean.parseBoolean( operationEnabledString );
            String operationClassName =  LdbcFinBenchTransactionWorkloadConfiguration.LDBC_FINBENCH_TRANSACTION_PACKAGE_PREFIX +
                    LdbcFinBenchTransactionWorkloadConfiguration.removePrefix(
                        LdbcFinBenchTransactionWorkloadConfiguration.removeSuffix(
                            operationEnableKey,
                            LdbcFinBenchTransactionWorkloadConfiguration.ENABLE_SUFFIX
                        ),
                        LdbcFinBenchTransactionWorkloadConfiguration
                            .LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX
                    );
            try
            {
                Class operationClass = ClassLoaderHelper.loadClass( operationClassName );
                if ( operationEnabled )
                {
                    enabledOperationTypes.add( operationClass );
                }

            }
            catch ( ClassLoadingException e )
            {
                throw new WorkloadException(
                    format(
                        "Unable to load operation class for parameter: %s%nGuessed incorrect class name: %s",
                        operationEnableKey, operationClassName ),
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
        if (runnableBatchLoader != null && !runnableBatchLoader.isInterrupted()){
            runnableBatchLoader.interrupt();
        }
    }

    /**
     * Peek the first operation and fetch the operation start time.
     * @param updateStream The Iterator with update operations
     * @param workloadStartTimeAsMilli The initial start time as milli
     * @return New workload start time as milli
     * @throws WorkloadException
     */
    private long getOperationStreamStartTime(
        Iterator<Operation> updateStream,
        long workloadStartTimeAsMilli
    ) throws WorkloadException
    {
        PeekingIterator<Operation> unfilteredUpdateOperations = Iterators.peekingIterator( updateStream );
        try
        {
            if ( unfilteredUpdateOperations.hasNext() && unfilteredUpdateOperations.peek().scheduledStartTimeAsMilli() < workloadStartTimeAsMilli )
            {
                workloadStartTimeAsMilli = unfilteredUpdateOperations.peek().scheduledStartTimeAsMilli();
            }
        }
        catch ( NoSuchElementException e )
        {
            // do nothing, exception just means that stream was empty
        }
        return workloadStartTimeAsMilli;
    }


    /**
     * Get the operation streams (substitution parameters)
     * @param gf Generator factory to use
     * @param workloadStartTimeAsMilli The workloadStartTimeAsMilli
     * @param loader Loader to open the csv files
     * @return List of operation streams
     * @throws WorkloadException
     */
    private List<Iterator<?>> getOperationStreams(
        GeneratorFactory gf,
        long workloadStartTimeAsMilli,
        ParquetLoader loader
    ) throws WorkloadException
    {
        List<Iterator<?>> asynchronousNonDependencyStreamsList = new ArrayList<>();
        /*
         * Create read operation streams, with specified interleaves
         */
        OperationStreamReader readOperationStream = new OperationStreamReader(loader);
        Map<Integer, EventStreamReader.EventDecoder<Operation>> decoders = QueryEventStreamReader.getDecoders();
        Map<Class<? extends Operation>, Integer> classToTypeMap = MapUtils.invertMap(operationTypeToClassMapping());
        for (Class enabledClass : enabledLongReadOperationTypes) {
            Integer type = classToTypeMap.get( enabledClass );
            Iterator<Operation> eventOperationStream = readOperationStream.readOperationStream(
                decoders.get(type),
                new File( parametersDir, LdbcFinBenchTransactionWorkloadConfiguration.READ_OPERATION_PARAMS_FILENAMES.get( type ))
            );
            long readOperationInterleaveAsMilli = longReadInterleavesAsMilli.get( type );
            Iterator<Long> operationStartTimes =
                gf.incrementing( workloadStartTimeAsMilli + readOperationInterleaveAsMilli,
                    readOperationInterleaveAsMilli );

            Iterator<Operation> operationStream = gf.assignStartTimes(
                operationStartTimes,
                new QueryEventStreamReader(gf.repeating( eventOperationStream ))
            );
            asynchronousNonDependencyStreamsList.add( operationStream );
        }
        return asynchronousNonDependencyStreamsList;
    }

    /**
     * Create Short read operations
     * @param hasDbConnected: Whether a database is connected
     * @return
     */
    private LdbcFinbenchShortReadGenerator getShortReadGenerator(boolean hasDbConnected)
    {
        RandomDataGeneratorFactory randomFactory = new RandomDataGeneratorFactory( 42l );
        double initialProbability = 1.0;

        Queue<Long> personIdBuffer;
        Queue<Long> messageIdBuffer;
        // LdbcFinbenchShortReadGenerator.SCHEDULED_START_TIME_POLICY scheduledStartTimePolicy;
        // LdbcFinbenchShortReadGenerator.BufferReplenishFun bufferReplenishFun;
        if (hasDbConnected)
        {
            // TODO add new Short Read Rule
            /*personIdBuffer = LdbcSnbShortReadGenerator.synchronizedCircularQueueBuffer( 1024 );
            messageIdBuffer = LdbcSnbShortReadGenerator.synchronizedCircularQueueBuffer( 1024 );
            scheduledStartTimePolicy = LdbcSnbShortReadGenerator.SCHEDULED_START_TIME_POLICY.PREVIOUS_OPERATION_ACTUAL_FINISH_TIME;
            bufferReplenishFun = new LdbcSnbShortReadGenerator.ResultBufferReplenishFun(personIdBuffer, messageIdBuffer );*/
        }
        else
        {
            // TODO add new Short Read Rule
            /*personIdBuffer = LdbcSnbShortReadGenerator.constantBuffer( 1 );
            messageIdBuffer = LdbcSnbShortReadGenerator.constantBuffer( 1 );
            scheduledStartTimePolicy = LdbcSnbShortReadGenerator.SCHEDULED_START_TIME_POLICY.PREVIOUS_OPERATION_SCHEDULED_START_TIME;
            bufferReplenishFun = new LdbcSnbShortReadGenerator.NoOpBufferReplenishFun();*/
        }
        return null;
        /*return new LdbcFinbenchShortReadGenerator(
            initialProbability,
            shortReadDissipationFactor,
            updateInterleaveAsMilli,
            enabledShortReadOperationTypes,
            compressionRatio,
            personIdBuffer,
            messageIdBuffer,
            randomFactory,
            longReadInterleavesAsMilli,
            scheduledStartTimePolicy,
            bufferReplenishFun
        );*/
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

        ParquetLoader loader;
        try {
            DuckDbParquetExtractor db = new DuckDbParquetExtractor();
            loader = new ParquetLoader(db);
        }
        catch (SQLException e){
            throw new WorkloadException(format("Error creating loader for operation streams %s", e));
        }

        ParquetLoader updateLoader;
        try {
            DuckDbParquetExtractor db = new DuckDbParquetExtractor();
            updateLoader = new ParquetLoader(db);
        }
        catch (SQLException e){
            throw new WorkloadException(format("Error creating updateLoader for operation streams %s", e));
        }


        /*
         * WRITES
         */
        if (!enabledUpdateOperationTypes.isEmpty())
        {
            asynchronousDependencyStreams = setBatchedUpdateStreams(gf, workloadStartTimeAsMilli, updateLoader);
            workloadStartTimeAsMilli = getOperationStreamStartTime(asynchronousDependencyStreams, workloadStartTimeAsMilli);
        }
        else
        {
            asynchronousDependencyStreams = Collections.emptyIterator();
        }

        if ( Long.MAX_VALUE == workloadStartTimeAsMilli )
        {
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
         * SHORT READS
         */
        ChildOperationGenerator shortReadsChildGenerator = null;
        if ( !enabledShortReadOperationTypes.isEmpty() )
        {
            shortReadsChildGenerator = getShortReadGenerator(hasDbConnected);
        }

        /*
         * FINAL STREAMS
         */
        ldbcFinbenchWorkloadStreams.setAsynchronousStream(
            dependentAsynchronousOperationTypes,
            dependencyAsynchronousOperationTypes,
            asynchronousDependencyStreams,
            asynchronousNonDependencyStreams,
            shortReadsChildGenerator
        );

        return ldbcFinbenchWorkloadStreams;
    }

    @Override
    public Set<Class> enabledValidationOperations() {
        Set<Class> enabledOperations = new HashSet<>();
        enabledOperations.addAll(enabledLongReadOperationTypes);
        enabledOperations.addAll(enabledUpdateOperationTypes);
        enabledOperations.addAll(enabledShortReadOperationTypes);
        return enabledOperations;
    }

    private Iterator<Operation> setBatchedUpdateStreams(
        GeneratorFactory gf,
        long workloadStartTimeAsMilli,
        ParquetLoader loader
    ) throws WorkloadException
    {
        long batchSizeInMillis = Math.round(TimeUnit.HOURS.toMillis( 1 ) * batchSize);

        Set<Class<? extends Operation>> dependencyUpdateOperationTypes = Sets.<Class<? extends Operation>>newHashSet();

        for (Class class1 : enabledUpdateOperationTypes) {
            dependencyUpdateOperationTypes.add(class1);
        }

        int batchQueueSize = LdbcFinBenchTransactionWorkloadConfiguration.BUFFERED_QUEUE_SIZE;

        BlockingQueue<Iterator<Operation>> blockingQueue = new LinkedBlockingQueue<>( batchQueueSize );
        runnableBatchLoader = new RunnableOperationStreamBatchLoader(
            loader,
            gf,
            updatesDir,
            blockingQueue,
            dependencyUpdateOperationTypes,
            batchSizeInMillis
        );
        runnableBatchLoader.start();

        OperationStreamBuffer buffer = new OperationStreamBuffer(blockingQueue);
        BufferedIterator bufferedIterator =  new BufferedIterator(buffer);
        bufferedIterator.init();
        return bufferedIterator;
    }
}
