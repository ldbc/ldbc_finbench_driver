package org.ldbcouncil.finbench.driver.workloads.transaction;

import static java.lang.String.format;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.control.ConsoleAndFileDriverConfiguration;
import org.ldbcouncil.finbench.driver.control.DriverConfigurationException;
import org.ldbcouncil.finbench.driver.util.FileUtils;
import org.ldbcouncil.finbench.driver.util.MapUtils;
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
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead6;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write10;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write11;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write12;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write13;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write14;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write15;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write16;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write17;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write18;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write19;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write6;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write7;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write8;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write9;

public abstract class LdbcFinBenchTransactionWorkloadConfiguration {
    public static final int WRITE_OPERATION_NO_RESULT_DEFAULT_RESULT = -1;
    public static final String LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX = "ldbc.finbench.transaction.queries.";
    // directory that contains the substitution parameters files
    public static final String PARAMETERS_DIRECTORY = LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + "parameters_dir";
    // directory containing forum and person update event streams
    public static final String UPDATES_DIRECTORY = LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + "updates_dir";
    // the parameter and updates files suffix
    public static final String FILES_SUFFIX = LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + "files_suffix";


    // Simple reads random walk dissipation rate, in the interval [1.0-0.0]
    // Higher values translate to shorter walks and therefore fewer simple reads
    public static final String simple_read_dissipation =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + "simple_read_dissipation";

    // Average distance between updates in simulation time
    public static final String UPDATE_INTERLEAVE = LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + "update_interleave";

    public static final String SCALE_FACTOR = LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + "scale_factor";
    public static final String UPDATE_STREAM_PARSER = LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + "update_parser";
    public static final UpdateStreamParser DEFAULT_UPDATE_STREAM_PARSER = UpdateStreamParser.CHAR_SEEKER;
    public static final String LDBC_transaction_PACKAGE_PREFIX =
        removeSuffix(ComplexRead1.class.getName(), ComplexRead1.class.getSimpleName());

    public static final String BATCH_SIZE = LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + "batch_size";

    // Default batch size denotes 24 hours of data
    public static final long DEFAULT_BATCH_SIZE = 24L;

    public static final int BUFFERED_QUEUE_SIZE = 4;
    public static final String INSERTS_DIRECTORY = "inserts";
    public static final String INSERTS_DATE_COLUMN = "createTime";
    public static final String DELETE_DATE_COLUMN = "deleteTime";
    public static final String FILE_SEPARATOR = ".";
    public static final String DEFAULT_FILE_SUFFIX = "csv";

    public static final String LDBC_FINBENCH_TRANSACTION_PACKAGE_PREFIX =
        removeSuffix(ComplexRead1.class.getName(), ComplexRead1.class.getSimpleName());

    /*
     * Operation Interleave
     */
    public static final String INTERLEAVE_SUFFIX = "_transaction";
    public static final String COMPLEX_READ_OPERATION_1_INTERLEAVE_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead1.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_2_INTERLEAVE_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead2.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_3_INTERLEAVE_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead3.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_4_INTERLEAVE_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead4.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_5_INTERLEAVE_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead5.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_6_INTERLEAVE_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead6.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_7_INTERLEAVE_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead7.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_8_INTERLEAVE_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead8.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_9_INTERLEAVE_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead9.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_10_INTERLEAVE_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead10.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_11_INTERLEAVE_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead11.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_12_INTERLEAVE_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead12.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public static final List<String> COMPLEX_READ_OPERATION_INTERLEAVE_KEYS = Lists.newArrayList(
        COMPLEX_READ_OPERATION_1_INTERLEAVE_KEY,
        COMPLEX_READ_OPERATION_2_INTERLEAVE_KEY,
        COMPLEX_READ_OPERATION_3_INTERLEAVE_KEY,
        COMPLEX_READ_OPERATION_4_INTERLEAVE_KEY,
        COMPLEX_READ_OPERATION_5_INTERLEAVE_KEY,
        COMPLEX_READ_OPERATION_6_INTERLEAVE_KEY,
        COMPLEX_READ_OPERATION_7_INTERLEAVE_KEY,
        COMPLEX_READ_OPERATION_8_INTERLEAVE_KEY,
        COMPLEX_READ_OPERATION_9_INTERLEAVE_KEY,
        COMPLEX_READ_OPERATION_10_INTERLEAVE_KEY,
        COMPLEX_READ_OPERATION_11_INTERLEAVE_KEY,
        COMPLEX_READ_OPERATION_12_INTERLEAVE_KEY);
    public static final Map<Integer, String> OPERATION_TYPE_TO_INTERLEAVE_KEY_MAPPING = typeToInterleaveKeyMapping();
    /*
     * Operation frequency
     */
    public static final String FREQUENCY_SUFFIX = "_freq";
    public static final String COMPLEX_READ_OPERATION_1_FREQUENCY_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead1.class.getSimpleName() + FREQUENCY_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_2_FREQUENCY_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead2.class.getSimpleName() + FREQUENCY_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_3_FREQUENCY_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead3.class.getSimpleName() + FREQUENCY_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_4_FREQUENCY_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead4.class.getSimpleName() + FREQUENCY_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_5_FREQUENCY_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead5.class.getSimpleName() + FREQUENCY_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_6_FREQUENCY_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead6.class.getSimpleName() + FREQUENCY_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_7_FREQUENCY_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead7.class.getSimpleName() + FREQUENCY_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_8_FREQUENCY_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead8.class.getSimpleName() + FREQUENCY_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_9_FREQUENCY_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead9.class.getSimpleName() + FREQUENCY_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_10_FREQUENCY_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead10.class.getSimpleName() + FREQUENCY_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_11_FREQUENCY_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead11.class.getSimpleName() + FREQUENCY_SUFFIX;
    public static final String COMPLEX_READ_OPERATION_12_FREQUENCY_KEY =
        LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead12.class.getSimpleName() + FREQUENCY_SUFFIX;
    public static final List<String> COMPLEX_READ_OPERATION_FREQUENCY_KEYS = Lists.newArrayList(
        COMPLEX_READ_OPERATION_1_FREQUENCY_KEY,
        COMPLEX_READ_OPERATION_2_FREQUENCY_KEY,
        COMPLEX_READ_OPERATION_3_FREQUENCY_KEY,
        COMPLEX_READ_OPERATION_4_FREQUENCY_KEY,
        COMPLEX_READ_OPERATION_5_FREQUENCY_KEY,
        COMPLEX_READ_OPERATION_6_FREQUENCY_KEY,
        COMPLEX_READ_OPERATION_7_FREQUENCY_KEY,
        COMPLEX_READ_OPERATION_8_FREQUENCY_KEY,
        COMPLEX_READ_OPERATION_9_FREQUENCY_KEY,
        COMPLEX_READ_OPERATION_10_FREQUENCY_KEY,
        COMPLEX_READ_OPERATION_11_FREQUENCY_KEY,
        COMPLEX_READ_OPERATION_12_FREQUENCY_KEY
    );
    // Default value in case there is no update stream
    public static final String DEFAULT_UPDATE_INTERLEAVE = "1";
    /*
     * Operation Enable
     */
    public static final String ENABLE_SUFFIX = "_enable";
    public static final String COMPLEX_READ_OPERATION_1_ENABLE_KEY = asEnableKey(ComplexRead1.class);
    public static final String COMPLEX_READ_OPERATION_2_ENABLE_KEY = asEnableKey(ComplexRead2.class);
    public static final String COMPLEX_READ_OPERATION_3_ENABLE_KEY = asEnableKey(ComplexRead3.class);
    public static final String COMPLEX_READ_OPERATION_4_ENABLE_KEY = asEnableKey(ComplexRead4.class);
    public static final String COMPLEX_READ_OPERATION_5_ENABLE_KEY = asEnableKey(ComplexRead5.class);
    public static final String COMPLEX_READ_OPERATION_6_ENABLE_KEY = asEnableKey(ComplexRead6.class);
    public static final String COMPLEX_READ_OPERATION_7_ENABLE_KEY = asEnableKey(ComplexRead7.class);
    public static final String COMPLEX_READ_OPERATION_8_ENABLE_KEY = asEnableKey(ComplexRead8.class);
    public static final String COMPLEX_READ_OPERATION_9_ENABLE_KEY = asEnableKey(ComplexRead9.class);
    public static final String COMPLEX_READ_OPERATION_10_ENABLE_KEY = asEnableKey(ComplexRead10.class);
    public static final String COMPLEX_READ_OPERATION_11_ENABLE_KEY = asEnableKey(ComplexRead11.class);
    public static final String COMPLEX_READ_OPERATION_12_ENABLE_KEY = asEnableKey(ComplexRead12.class);
    public static final List<String> COMPLEX_READ_OPERATION_ENABLE_KEYS = Lists.newArrayList(
        COMPLEX_READ_OPERATION_1_ENABLE_KEY,
        COMPLEX_READ_OPERATION_2_ENABLE_KEY,
        COMPLEX_READ_OPERATION_3_ENABLE_KEY,
        COMPLEX_READ_OPERATION_4_ENABLE_KEY,
        COMPLEX_READ_OPERATION_5_ENABLE_KEY,
        COMPLEX_READ_OPERATION_6_ENABLE_KEY,
        COMPLEX_READ_OPERATION_7_ENABLE_KEY,
        COMPLEX_READ_OPERATION_8_ENABLE_KEY,
        COMPLEX_READ_OPERATION_9_ENABLE_KEY,
        COMPLEX_READ_OPERATION_10_ENABLE_KEY,
        COMPLEX_READ_OPERATION_11_ENABLE_KEY,
        COMPLEX_READ_OPERATION_12_ENABLE_KEY
    );
    public static final String SIMPLE_READ_OPERATION_1_ENABLE_KEY = asEnableKey(SimpleRead1.class);
    public static final String SIMPLE_READ_OPERATION_2_ENABLE_KEY = asEnableKey(SimpleRead2.class);
    public static final String SIMPLE_READ_OPERATION_3_ENABLE_KEY = asEnableKey(SimpleRead3.class);
    public static final String SIMPLE_READ_OPERATION_4_ENABLE_KEY = asEnableKey(SimpleRead4.class);
    public static final String SIMPLE_READ_OPERATION_5_ENABLE_KEY = asEnableKey(SimpleRead5.class);
    public static final String SIMPLE_READ_OPERATION_6_ENABLE_KEY = asEnableKey(SimpleRead6.class);
    public static final List<String> SIMPLE_READ_OPERATION_ENABLE_KEYS = Lists.newArrayList(
        SIMPLE_READ_OPERATION_1_ENABLE_KEY,
        SIMPLE_READ_OPERATION_2_ENABLE_KEY,
        SIMPLE_READ_OPERATION_3_ENABLE_KEY,
        SIMPLE_READ_OPERATION_4_ENABLE_KEY,
        SIMPLE_READ_OPERATION_5_ENABLE_KEY,
        SIMPLE_READ_OPERATION_6_ENABLE_KEY
    );
    public static final String WRITE_OPERATION_1_ENABLE_KEY = asEnableKey(Write1.class);
    public static final String WRITE_OPERATION_2_ENABLE_KEY = asEnableKey(Write2.class);
    public static final String WRITE_OPERATION_3_ENABLE_KEY = asEnableKey(Write3.class);
    public static final String WRITE_OPERATION_4_ENABLE_KEY = asEnableKey(Write4.class);
    public static final String WRITE_OPERATION_5_ENABLE_KEY = asEnableKey(Write5.class);
    public static final String WRITE_OPERATION_6_ENABLE_KEY = asEnableKey(Write6.class);
    public static final String WRITE_OPERATION_7_ENABLE_KEY = asEnableKey(Write7.class);
    public static final String WRITE_OPERATION_8_ENABLE_KEY = asEnableKey(Write8.class);
    public static final String WRITE_OPERATION_9_ENABLE_KEY = asEnableKey(Write9.class);
    public static final String WRITE_OPERATION_10_ENABLE_KEY = asEnableKey(Write10.class);
    public static final String WRITE_OPERATION_11_ENABLE_KEY = asEnableKey(Write11.class);
    public static final String WRITE_OPERATION_12_ENABLE_KEY = asEnableKey(Write12.class);
    public static final String WRITE_OPERATION_13_ENABLE_KEY = asEnableKey(Write13.class);
    public static final String WRITE_OPERATION_14_ENABLE_KEY = asEnableKey(Write14.class);
    public static final String WRITE_OPERATION_15_ENABLE_KEY = asEnableKey(Write15.class);
    public static final String WRITE_OPERATION_16_ENABLE_KEY = asEnableKey(Write16.class);
    public static final String WRITE_OPERATION_17_ENABLE_KEY = asEnableKey(Write17.class);
    public static final String WRITE_OPERATION_18_ENABLE_KEY = asEnableKey(Write18.class);
    public static final String WRITE_OPERATION_19_ENABLE_KEY = asEnableKey(Write19.class);
    public static final String READ_WRITE_OPERATION_1_ENABLE_KEY = asEnableKey(ReadWrite1.class);
    public static final String READ_WRITE_OPERATION_2_ENABLE_KEY = asEnableKey(ReadWrite2.class);
    public static final String READ_WRITE_OPERATION_3_ENABLE_KEY = asEnableKey(ReadWrite3.class);
    public static final List<String> WRITE_OPERATION_ENABLE_KEYS = Lists.newArrayList(
        WRITE_OPERATION_1_ENABLE_KEY,
        WRITE_OPERATION_2_ENABLE_KEY,
        WRITE_OPERATION_3_ENABLE_KEY,
        WRITE_OPERATION_4_ENABLE_KEY,
        WRITE_OPERATION_5_ENABLE_KEY,
        WRITE_OPERATION_6_ENABLE_KEY,
        WRITE_OPERATION_7_ENABLE_KEY,
        WRITE_OPERATION_8_ENABLE_KEY,
        WRITE_OPERATION_9_ENABLE_KEY,
        WRITE_OPERATION_10_ENABLE_KEY,
        WRITE_OPERATION_11_ENABLE_KEY,
        WRITE_OPERATION_12_ENABLE_KEY,
        WRITE_OPERATION_13_ENABLE_KEY,
        WRITE_OPERATION_14_ENABLE_KEY,
        WRITE_OPERATION_15_ENABLE_KEY,
        WRITE_OPERATION_16_ENABLE_KEY,
        WRITE_OPERATION_17_ENABLE_KEY,
        WRITE_OPERATION_18_ENABLE_KEY,
        WRITE_OPERATION_19_ENABLE_KEY,
        READ_WRITE_OPERATION_1_ENABLE_KEY,
        READ_WRITE_OPERATION_2_ENABLE_KEY,
        READ_WRITE_OPERATION_3_ENABLE_KEY
    );
    /*
     * Read Operation Parameters
     */
    public static final String COMPLEX_READ_OPERATION_1_PARAMS_FILENAME = "complex_1_param";
    public static final String COMPLEX_READ_OPERATION_2_PARAMS_FILENAME = "complex_2_param";
    public static final String COMPLEX_READ_OPERATION_3_PARAMS_FILENAME = "complex_3_param";
    public static final String COMPLEX_READ_OPERATION_4_PARAMS_FILENAME = "complex_4_param";
    public static final String COMPLEX_READ_OPERATION_5_PARAMS_FILENAME = "complex_5_param";
    public static final String COMPLEX_READ_OPERATION_6_PARAMS_FILENAME = "complex_6_param";
    public static final String COMPLEX_READ_OPERATION_7_PARAMS_FILENAME = "complex_7_param";
    public static final String COMPLEX_READ_OPERATION_8_PARAMS_FILENAME = "complex_8_param";
    public static final String COMPLEX_READ_OPERATION_9_PARAMS_FILENAME = "complex_9_param";
    public static final String COMPLEX_READ_OPERATION_10_PARAMS_FILENAME = "complex_10_param";
    public static final String COMPLEX_READ_OPERATION_11_PARAMS_FILENAME = "complex_11_param";
    public static final String COMPLEX_READ_OPERATION_12_PARAMS_FILENAME = "complex_12_param";
    public static final Map<Integer, String> COMPLEX_READ_OPERATION_PARAMS_FILENAMES =
        typeToOperationParameterFilename();
    /*
     * Write Operation Parameters
     */
    public static final String PIPE_SEPARATOR_REGEX = "\\|";

    private static Map<Integer, String> typeToOperationParameterFilename() {
        Map<Integer, String> mapping = new HashMap<>();
        mapping.put(ComplexRead1.TYPE, COMPLEX_READ_OPERATION_1_PARAMS_FILENAME);
        mapping.put(ComplexRead2.TYPE, COMPLEX_READ_OPERATION_2_PARAMS_FILENAME);
        mapping.put(ComplexRead3.TYPE, COMPLEX_READ_OPERATION_3_PARAMS_FILENAME);
        mapping.put(ComplexRead4.TYPE, COMPLEX_READ_OPERATION_4_PARAMS_FILENAME);
        mapping.put(ComplexRead5.TYPE, COMPLEX_READ_OPERATION_5_PARAMS_FILENAME);
        mapping.put(ComplexRead6.TYPE, COMPLEX_READ_OPERATION_6_PARAMS_FILENAME);
        mapping.put(ComplexRead7.TYPE, COMPLEX_READ_OPERATION_7_PARAMS_FILENAME);
        mapping.put(ComplexRead8.TYPE, COMPLEX_READ_OPERATION_8_PARAMS_FILENAME);
        mapping.put(ComplexRead9.TYPE, COMPLEX_READ_OPERATION_9_PARAMS_FILENAME);
        mapping.put(ComplexRead10.TYPE, COMPLEX_READ_OPERATION_10_PARAMS_FILENAME);
        mapping.put(ComplexRead11.TYPE, COMPLEX_READ_OPERATION_11_PARAMS_FILENAME);
        mapping.put(ComplexRead12.TYPE, COMPLEX_READ_OPERATION_12_PARAMS_FILENAME);
        return mapping;
    }

    private static Map<Integer, String> typeToInterleaveKeyMapping() {
        Map<Integer, String> mapping = new HashMap<>();
        mapping.put(ComplexRead1.TYPE, COMPLEX_READ_OPERATION_1_INTERLEAVE_KEY);
        mapping.put(ComplexRead2.TYPE, COMPLEX_READ_OPERATION_2_INTERLEAVE_KEY);
        mapping.put(ComplexRead3.TYPE, COMPLEX_READ_OPERATION_3_INTERLEAVE_KEY);
        mapping.put(ComplexRead4.TYPE, COMPLEX_READ_OPERATION_4_INTERLEAVE_KEY);
        mapping.put(ComplexRead5.TYPE, COMPLEX_READ_OPERATION_5_INTERLEAVE_KEY);
        mapping.put(ComplexRead6.TYPE, COMPLEX_READ_OPERATION_6_INTERLEAVE_KEY);
        mapping.put(ComplexRead7.TYPE, COMPLEX_READ_OPERATION_7_INTERLEAVE_KEY);
        mapping.put(ComplexRead8.TYPE, COMPLEX_READ_OPERATION_8_INTERLEAVE_KEY);
        mapping.put(ComplexRead9.TYPE, COMPLEX_READ_OPERATION_9_INTERLEAVE_KEY);
        mapping.put(ComplexRead10.TYPE, COMPLEX_READ_OPERATION_10_INTERLEAVE_KEY);
        mapping.put(ComplexRead11.TYPE, COMPLEX_READ_OPERATION_11_INTERLEAVE_KEY);
        mapping.put(ComplexRead12.TYPE, COMPLEX_READ_OPERATION_12_INTERLEAVE_KEY);
        return mapping;
    }

    private static String asEnableKey(Class<? extends Operation> operation) {
        return LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + operation.getSimpleName() + ENABLE_SUFFIX;
    }

    public static Map<String, String> convertFrequenciesToInterleaves(Map<String, String> params) {
        Integer updateDistance = Integer.parseInt(params.get(UPDATE_INTERLEAVE));

        Integer interleave = Integer.parseInt(params.get(COMPLEX_READ_OPERATION_1_FREQUENCY_KEY)) * updateDistance;
        params.put(COMPLEX_READ_OPERATION_1_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(COMPLEX_READ_OPERATION_2_FREQUENCY_KEY)) * updateDistance;
        params.put(COMPLEX_READ_OPERATION_2_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(COMPLEX_READ_OPERATION_3_FREQUENCY_KEY)) * updateDistance;
        params.put(COMPLEX_READ_OPERATION_3_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(COMPLEX_READ_OPERATION_4_FREQUENCY_KEY)) * updateDistance;
        params.put(COMPLEX_READ_OPERATION_4_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(COMPLEX_READ_OPERATION_5_FREQUENCY_KEY)) * updateDistance;
        params.put(COMPLEX_READ_OPERATION_5_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(COMPLEX_READ_OPERATION_6_FREQUENCY_KEY)) * updateDistance;
        params.put(COMPLEX_READ_OPERATION_6_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(COMPLEX_READ_OPERATION_7_FREQUENCY_KEY)) * updateDistance;
        params.put(COMPLEX_READ_OPERATION_7_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(COMPLEX_READ_OPERATION_8_FREQUENCY_KEY)) * updateDistance;
        params.put(COMPLEX_READ_OPERATION_8_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(COMPLEX_READ_OPERATION_9_FREQUENCY_KEY)) * updateDistance;
        params.put(COMPLEX_READ_OPERATION_9_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(COMPLEX_READ_OPERATION_10_FREQUENCY_KEY)) * updateDistance;
        params.put(COMPLEX_READ_OPERATION_10_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(COMPLEX_READ_OPERATION_11_FREQUENCY_KEY)) * updateDistance;
        params.put(COMPLEX_READ_OPERATION_11_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(COMPLEX_READ_OPERATION_12_FREQUENCY_KEY)) * updateDistance;
        params.put(COMPLEX_READ_OPERATION_12_INTERLEAVE_KEY, interleave.toString());

        return params;
    }

    public static Map<String, String> defaultConfigSF1() throws IOException {
        String filename = "/configuration/ldbc/finbench/transaction/sf_internal_test.properties";
        return ConsoleAndFileDriverConfiguration.convertComplexKeysToSimpleKeys(resourceToMap(filename));
    }

    private static Map<String, String> resourceToMap(String filename) throws IOException {
        try (InputStream inputStream = LdbcFinBenchTransactionWorkloadConfiguration.class.getResource(filename)
            .openStream()) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return new HashMap<>(Maps.fromProperties(properties));
        }
    }

    public static Map<String, String> defaultReadOnlyConfigSF1() throws DriverConfigurationException, IOException {
        Map<String, String> params = withoutWrites(
            defaultConfigSF1()
        );
        return ConsoleAndFileDriverConfiguration.convertComplexKeysToSimpleKeys(params);
    }

    public static Map<String, String> withOnly(
        Map<String, String> originalParams,
        Class<? extends Operation>... operationClasses)
        throws DriverConfigurationException, IOException {
        Map<String, String> params = withoutWrites(
            withoutSimpleReads(
                withoutLongReads(originalParams)
            )
        );
        for (Class<? extends Operation> operationClass : operationClasses) {
            params.put(asEnableKey(operationClass), "true");
        }
        return ConsoleAndFileDriverConfiguration.convertComplexKeysToSimpleKeys(params);
    }

    public static boolean hasReads(Map<String, String> params) {
        return Lists.newArrayList(
            COMPLEX_READ_OPERATION_1_ENABLE_KEY,
            COMPLEX_READ_OPERATION_2_ENABLE_KEY,
            COMPLEX_READ_OPERATION_3_ENABLE_KEY,
            COMPLEX_READ_OPERATION_4_ENABLE_KEY,
            COMPLEX_READ_OPERATION_5_ENABLE_KEY,
            COMPLEX_READ_OPERATION_6_ENABLE_KEY,
            COMPLEX_READ_OPERATION_7_ENABLE_KEY,
            COMPLEX_READ_OPERATION_8_ENABLE_KEY,
            COMPLEX_READ_OPERATION_9_ENABLE_KEY,
            COMPLEX_READ_OPERATION_10_ENABLE_KEY,
            COMPLEX_READ_OPERATION_11_ENABLE_KEY,
            COMPLEX_READ_OPERATION_12_ENABLE_KEY).stream().anyMatch(key -> isSet(params, key));
    }

    public static boolean hasWrites(Map<String, String> params) {
        return Lists.newArrayList(
            WRITE_OPERATION_1_ENABLE_KEY,
            WRITE_OPERATION_2_ENABLE_KEY,
            WRITE_OPERATION_3_ENABLE_KEY,
            WRITE_OPERATION_4_ENABLE_KEY,
            WRITE_OPERATION_5_ENABLE_KEY,
            WRITE_OPERATION_6_ENABLE_KEY,
            WRITE_OPERATION_7_ENABLE_KEY,
            WRITE_OPERATION_8_ENABLE_KEY).stream().anyMatch(key -> isSet(params, key));
    }

    private static boolean isSet(Map<String, String> params, String key) {
        return params.containsKey(key) && null != params.get(key) && Boolean.parseBoolean(params.get(key));
    }

    public static Map<String, String> withoutSimpleReads(Map<String, String> originalParams)
        throws DriverConfigurationException, IOException {
        Map<String, String> params = MapUtils.copyExcludingKeys(originalParams, new HashSet<>());
        params.put(SIMPLE_READ_OPERATION_1_ENABLE_KEY, "false");
        params.put(SIMPLE_READ_OPERATION_2_ENABLE_KEY, "false");
        params.put(SIMPLE_READ_OPERATION_3_ENABLE_KEY, "false");
        params.put(SIMPLE_READ_OPERATION_4_ENABLE_KEY, "false");
        params.put(SIMPLE_READ_OPERATION_5_ENABLE_KEY, "false");
        params.put(SIMPLE_READ_OPERATION_6_ENABLE_KEY, "false");
        return ConsoleAndFileDriverConfiguration.convertComplexKeysToSimpleKeys(params);
    }

    public static Map<String, String> withoutWrites(Map<String, String> originalParams)
        throws DriverConfigurationException, IOException {
        Map<String, String> params = MapUtils.copyExcludingKeys(originalParams, new HashSet<>());
        params.put(WRITE_OPERATION_1_ENABLE_KEY, "false");
        params.put(WRITE_OPERATION_2_ENABLE_KEY, "false");
        params.put(WRITE_OPERATION_3_ENABLE_KEY, "false");
        params.put(WRITE_OPERATION_4_ENABLE_KEY, "false");
        params.put(WRITE_OPERATION_5_ENABLE_KEY, "false");
        params.put(WRITE_OPERATION_6_ENABLE_KEY, "false");
        params.put(WRITE_OPERATION_7_ENABLE_KEY, "false");
        params.put(WRITE_OPERATION_8_ENABLE_KEY, "false");
        return ConsoleAndFileDriverConfiguration.convertComplexKeysToSimpleKeys(params);
    }

    public static Map<String, String> withoutLongReads(Map<String, String> originalParams)
        throws DriverConfigurationException, IOException {
        Map<String, String> params = MapUtils.copyExcludingKeys(originalParams, new HashSet<String>());
        params.put(COMPLEX_READ_OPERATION_1_ENABLE_KEY, "false");
        params.put(COMPLEX_READ_OPERATION_2_ENABLE_KEY, "false");
        params.put(COMPLEX_READ_OPERATION_3_ENABLE_KEY, "false");
        params.put(COMPLEX_READ_OPERATION_4_ENABLE_KEY, "false");
        params.put(COMPLEX_READ_OPERATION_5_ENABLE_KEY, "false");
        params.put(COMPLEX_READ_OPERATION_6_ENABLE_KEY, "false");
        params.put(COMPLEX_READ_OPERATION_7_ENABLE_KEY, "false");
        params.put(COMPLEX_READ_OPERATION_8_ENABLE_KEY, "false");
        params.put(COMPLEX_READ_OPERATION_9_ENABLE_KEY, "false");
        params.put(COMPLEX_READ_OPERATION_10_ENABLE_KEY, "false");
        params.put(COMPLEX_READ_OPERATION_11_ENABLE_KEY, "false");
        params.put(COMPLEX_READ_OPERATION_12_ENABLE_KEY, "false");
        params.put(SIMPLE_READ_OPERATION_1_ENABLE_KEY, "false");
        params.put(SIMPLE_READ_OPERATION_2_ENABLE_KEY, "false");
        params.put(SIMPLE_READ_OPERATION_3_ENABLE_KEY, "false");
        params.put(SIMPLE_READ_OPERATION_4_ENABLE_KEY, "false");
        params.put(SIMPLE_READ_OPERATION_5_ENABLE_KEY, "false");
        params.put(SIMPLE_READ_OPERATION_6_ENABLE_KEY, "false");
        return ConsoleAndFileDriverConfiguration.convertComplexKeysToSimpleKeys(params);
    }

    public static Map<Integer, Class<? extends Operation>> operationTypeToClassMapping() {
        Map<Integer, Class<? extends Operation>> operationTypeToClassMapping = new HashMap<>();
        operationTypeToClassMapping.put(ComplexRead1.TYPE, ComplexRead1.class);
        operationTypeToClassMapping.put(ComplexRead2.TYPE, ComplexRead2.class);
        operationTypeToClassMapping.put(ComplexRead3.TYPE, ComplexRead3.class);
        operationTypeToClassMapping.put(ComplexRead4.TYPE, ComplexRead4.class);
        operationTypeToClassMapping.put(ComplexRead5.TYPE, ComplexRead5.class);
        operationTypeToClassMapping.put(ComplexRead6.TYPE, ComplexRead6.class);
        operationTypeToClassMapping.put(ComplexRead7.TYPE, ComplexRead7.class);
        operationTypeToClassMapping.put(ComplexRead8.TYPE, ComplexRead8.class);
        operationTypeToClassMapping.put(ComplexRead9.TYPE, ComplexRead9.class);
        operationTypeToClassMapping.put(ComplexRead10.TYPE, ComplexRead10.class);
        operationTypeToClassMapping.put(ComplexRead11.TYPE, ComplexRead11.class);
        operationTypeToClassMapping.put(ComplexRead12.TYPE, ComplexRead12.class);
        operationTypeToClassMapping.put(SimpleRead1.TYPE, SimpleRead1.class);
        operationTypeToClassMapping.put(SimpleRead2.TYPE, SimpleRead2.class);
        operationTypeToClassMapping.put(SimpleRead3.TYPE, SimpleRead3.class);
        operationTypeToClassMapping.put(SimpleRead4.TYPE, SimpleRead4.class);
        operationTypeToClassMapping.put(SimpleRead5.TYPE, SimpleRead5.class);
        operationTypeToClassMapping.put(SimpleRead6.TYPE, SimpleRead6.class);
        operationTypeToClassMapping.put(Write1.TYPE, Write1.class);
        operationTypeToClassMapping.put(Write2.TYPE, Write2.class);
        operationTypeToClassMapping.put(Write3.TYPE, Write3.class);
        operationTypeToClassMapping.put(Write4.TYPE, Write4.class);
        operationTypeToClassMapping.put(Write5.TYPE, Write5.class);
        operationTypeToClassMapping.put(Write6.TYPE, Write6.class);
        operationTypeToClassMapping.put(Write7.TYPE, Write7.class);
        operationTypeToClassMapping.put(Write8.TYPE, Write8.class);
        operationTypeToClassMapping.put(Write9.TYPE, Write9.class);
        operationTypeToClassMapping.put(Write10.TYPE, Write10.class);
        operationTypeToClassMapping.put(Write11.TYPE, Write11.class);
        operationTypeToClassMapping.put(Write12.TYPE, Write12.class);
        operationTypeToClassMapping.put(Write13.TYPE, Write13.class);
        operationTypeToClassMapping.put(Write14.TYPE, Write14.class);
        operationTypeToClassMapping.put(Write15.TYPE, Write15.class);
        operationTypeToClassMapping.put(Write16.TYPE, Write16.class);
        operationTypeToClassMapping.put(Write17.TYPE, Write17.class);
        operationTypeToClassMapping.put(Write18.TYPE, Write18.class);
        operationTypeToClassMapping.put(Write19.TYPE, Write19.class);
        operationTypeToClassMapping.put(ReadWrite1.TYPE, ReadWrite1.class);
        operationTypeToClassMapping.put(ReadWrite2.TYPE, ReadWrite2.class);
        operationTypeToClassMapping.put(ReadWrite3.TYPE, ReadWrite3.class);
        return operationTypeToClassMapping;
    }

    static String removeSuffix(String original, String suffix) {
        return (!original.contains(suffix)) ? original : original.substring(0, original.lastIndexOf(suffix));
    }

    static String removePrefix(String original, String prefix) {
        return (!original.contains(prefix)) ? original : original
            .substring(original.lastIndexOf(prefix) + prefix.length());
    }

    static Set<String> missingParameters(Map<String, String> properties, Iterable<String> compulsoryPropertyKeys) {
        Set<String> missingPropertyKeys = new HashSet<>();
        for (String compulsoryKey : compulsoryPropertyKeys) {
            if (null == properties.get(compulsoryKey)) {
                missingPropertyKeys.add(compulsoryKey);
            }
        }
        return missingPropertyKeys;
    }

    static boolean isValidParser(String parserString) throws WorkloadException {
        try {
            UpdateStreamParser parser = UpdateStreamParser.valueOf(parserString);
            Set<UpdateStreamParser> validParsers = new HashSet<>();
            validParsers.addAll(Arrays.asList(UpdateStreamParser.values()));
            return validParsers.contains(parser);
        } catch (IllegalArgumentException e) {
            throw new WorkloadException(format("Unsupported parser value: %s", parserString), e);
        }
    }

    public static List<File> forumUpdateFilesInDirectory(File directory) {
        return FileUtils.filesWithSuffixInDirectory(directory, "_forum.csv");
    }

    public static List<File> personUpdateFilesInDirectory(File directory) {
        return FileUtils.filesWithSuffixInDirectory(directory, "_person.csv");
    }

    /**
     * Get mapping of update operation and filename containing the events
     */
    public static Map<Class<? extends Operation>, String> getUpdateStreamClassToPathMapping() {
        Map<Class<? extends Operation>, String> classToFileNameMapping = new HashMap<>();
        // Inserts
        classToFileNameMapping.put(Write1.class, "/AddPersonWrite1");
        classToFileNameMapping.put(Write2.class, "/AddCompanyWrite2");
        classToFileNameMapping.put(Write3.class, "/AddMediumWrite3");
        classToFileNameMapping.put(Write4.class, "/AddPersonOwnAccountWrite4");
        classToFileNameMapping.put(Write5.class, "/AddCompanyOwnAccountWrite5");
        classToFileNameMapping.put(Write6.class, "/AddPersonApplyLoanWrite6");
        classToFileNameMapping.put(Write7.class, "/AddCompanyApplyLoanWrite7");
        classToFileNameMapping.put(Write8.class, "/AddPersonInvestCompanyWrite8");
        classToFileNameMapping.put(Write9.class, "/AddCompanyInvestCompanyWrite9");
        classToFileNameMapping.put(Write10.class, "/AddPersonGuaranteePersonWrite10");
        classToFileNameMapping.put(Write11.class, "/AddCompanyGuaranteeCompanyWrite11");
        classToFileNameMapping.put(Write12.class, "/AddAccountTransferAccountWrite12");
        classToFileNameMapping.put(Write13.class, "/AddAccountWithdrawAccountWrite13");
        classToFileNameMapping.put(Write14.class, "/AddAccountRepayLoanWrite14");
        classToFileNameMapping.put(Write15.class, "/AddLoanDepositAccountWrite15");
        classToFileNameMapping.put(Write16.class, "/AddMediumSigninAccountWrite16");
        classToFileNameMapping.put(Write17.class, "/DeleteAccountWrite17");
        classToFileNameMapping.put(Write18.class, "/UpdateAccountWrite18");
        classToFileNameMapping.put(Write19.class, "/UpdatePersonWrite19");
        classToFileNameMapping.put(ReadWrite1.class, "/AddAccountTransferAccountReadWrite1");
        classToFileNameMapping.put(ReadWrite2.class, "/AddAccountTransferAccountReadWrite2");
        classToFileNameMapping.put(ReadWrite3.class, "/AddPersonGuaranteePersonReadWrite3");
        return classToFileNameMapping;
    }

    /**
     * Get mapping of update operation and filename containing the events
     */
    public static Map<Class<? extends Operation>, String> getUpdateStreamClassToDateColumn() {
        Map<Class<? extends Operation>, String> classToDateColumnNameMapping = new HashMap<>();
        classToDateColumnNameMapping.put(Write1.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write2.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write3.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write4.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write5.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write6.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write7.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write8.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write9.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write10.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write11.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write12.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write13.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write14.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write15.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write16.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write17.class, DELETE_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write18.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(Write19.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(ReadWrite1.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(ReadWrite2.class, INSERTS_DATE_COLUMN);
        classToDateColumnNameMapping.put(ReadWrite3.class, INSERTS_DATE_COLUMN);
        return classToDateColumnNameMapping;
    }

    // The parser implementation to use when reading update events
    public enum UpdateStreamParser {
        REGEX,
        CHAR_SEEKER,
        CHAR_SEEKER_THREAD
    }
}
