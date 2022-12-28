package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.control.ConsoleAndFileDriverConfiguration;
import org.ldbcouncil.finbench.driver.control.DriverConfigurationException;
import org.ldbcouncil.finbench.driver.util.FileUtils;
import org.ldbcouncil.finbench.driver.util.MapUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static java.lang.String.format;

public abstract class LdbcFinBenchTransactionWorkloadConfiguration {
    public static final int WRITE_OPERATION_NO_RESULT_DEFAULT_RESULT = -1;
    public final static String LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX = "ldbc.finbench.transaction.";
    // directory that contains the substitution parameters files
    public final static String PARAMETERS_DIRECTORY = LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + "parameters_dir";
    // directory containing forum and person update event streams
    public final static String UPDATES_DIRECTORY = LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + "updates_dir";

    // Short reads random walk dissipation rate, in the interval [1.0-0.0]
    // Higher values translate to shorter walks and therefore fewer short reads
    public final static String SHORT_READ_DISSIPATION =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + "short_read_dissipation";

    // Average distance between updates in simulation time
    public final static String UPDATE_INTERLEAVE = LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + "update_interleave";

    public final static String SCALE_FACTOR = LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + "scale_factor";

    // The parser implementation to use when reading update events
    public enum UpdateStreamParser {
        REGEX,
        CHAR_SEEKER,
        CHAR_SEEKER_THREAD
    }

    public final static String UPDATE_STREAM_PARSER = LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + "update_parser";
    public final static UpdateStreamParser DEFAULT_UPDATE_STREAM_PARSER = UpdateStreamParser.CHAR_SEEKER;
    public final static String LDBC_transaction_PACKAGE_PREFIX =
            removeSuffix(ComplexRead1.class.getName(), ComplexRead1.class.getSimpleName());

    /*
     * Operation Interleave
     */
    public final static String INTERLEAVE_SUFFIX = "_interleave";
    public final static String READ_OPERATION_1_INTERLEAVE_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead1.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public final static String READ_OPERATION_2_INTERLEAVE_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead2.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public final static String READ_OPERATION_3_INTERLEAVE_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead3.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public final static String READ_OPERATION_4_INTERLEAVE_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead4.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public final static String READ_OPERATION_5_INTERLEAVE_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead5.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public final static String READ_OPERATION_6_INTERLEAVE_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead6.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public final static String READ_OPERATION_7_INTERLEAVE_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead7.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public final static String READ_OPERATION_8_INTERLEAVE_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead8.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public final static String READ_OPERATION_9_INTERLEAVE_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead9.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public final static String READ_OPERATION_10_INTERLEAVE_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead10.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public final static String READ_OPERATION_11_INTERLEAVE_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead11.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public final static String READ_OPERATION_12_INTERLEAVE_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead12.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public final static String READ_OPERATION_13_INTERLEAVE_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead13.class.getSimpleName() + INTERLEAVE_SUFFIX;
    public final static List<String> READ_OPERATION_INTERLEAVE_KEYS = Lists.newArrayList(
            READ_OPERATION_1_INTERLEAVE_KEY,
            READ_OPERATION_2_INTERLEAVE_KEY,
            READ_OPERATION_3_INTERLEAVE_KEY,
            READ_OPERATION_4_INTERLEAVE_KEY,
            READ_OPERATION_5_INTERLEAVE_KEY,
            READ_OPERATION_6_INTERLEAVE_KEY,
            READ_OPERATION_7_INTERLEAVE_KEY,
            READ_OPERATION_8_INTERLEAVE_KEY,
            READ_OPERATION_9_INTERLEAVE_KEY,
            READ_OPERATION_10_INTERLEAVE_KEY,
            READ_OPERATION_11_INTERLEAVE_KEY,
            READ_OPERATION_12_INTERLEAVE_KEY,
            READ_OPERATION_13_INTERLEAVE_KEY);

    /*
     * Operation frequency
     */
    public final static String FREQUENCY_SUFFIX = "_freq";
    public final static String READ_OPERATION_1_FREQUENCY_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead1.class.getSimpleName() + FREQUENCY_SUFFIX;
    public final static String READ_OPERATION_2_FREQUENCY_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead2.class.getSimpleName() + FREQUENCY_SUFFIX;
    public final static String READ_OPERATION_3_FREQUENCY_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead3.class.getSimpleName() + FREQUENCY_SUFFIX;
    public final static String READ_OPERATION_4_FREQUENCY_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead4.class.getSimpleName() + FREQUENCY_SUFFIX;
    public final static String READ_OPERATION_5_FREQUENCY_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead5.class.getSimpleName() + FREQUENCY_SUFFIX;
    public final static String READ_OPERATION_6_FREQUENCY_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead6.class.getSimpleName() + FREQUENCY_SUFFIX;
    public final static String READ_OPERATION_7_FREQUENCY_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead7.class.getSimpleName() + FREQUENCY_SUFFIX;
    public final static String READ_OPERATION_8_FREQUENCY_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead8.class.getSimpleName() + FREQUENCY_SUFFIX;
    public final static String READ_OPERATION_9_FREQUENCY_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead9.class.getSimpleName() + FREQUENCY_SUFFIX;
    public final static String READ_OPERATION_10_FREQUENCY_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead10.class.getSimpleName() + FREQUENCY_SUFFIX;
    public final static String READ_OPERATION_11_FREQUENCY_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead11.class.getSimpleName() + FREQUENCY_SUFFIX;
    public final static String READ_OPERATION_12_FREQUENCY_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead12.class.getSimpleName() + FREQUENCY_SUFFIX;
    public final static String READ_OPERATION_13_FREQUENCY_KEY =
            LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + ComplexRead13.class.getSimpleName() + FREQUENCY_SUFFIX;
    public final static List<String> READ_OPERATION_FREQUENCY_KEYS = Lists.newArrayList(
            READ_OPERATION_1_FREQUENCY_KEY,
            READ_OPERATION_2_FREQUENCY_KEY,
            READ_OPERATION_3_FREQUENCY_KEY,
            READ_OPERATION_4_FREQUENCY_KEY,
            READ_OPERATION_5_FREQUENCY_KEY,
            READ_OPERATION_6_FREQUENCY_KEY,
            READ_OPERATION_7_FREQUENCY_KEY,
            READ_OPERATION_8_FREQUENCY_KEY,
            READ_OPERATION_9_FREQUENCY_KEY,
            READ_OPERATION_10_FREQUENCY_KEY,
            READ_OPERATION_11_FREQUENCY_KEY,
            READ_OPERATION_12_FREQUENCY_KEY,
            READ_OPERATION_13_FREQUENCY_KEY
    );

    private static Map<Integer, String> typeToInterleaveKeyMapping() {
        Map<Integer, String> mapping = new HashMap<>();
        mapping.put(ComplexRead1.TYPE, READ_OPERATION_1_INTERLEAVE_KEY);
        mapping.put(ComplexRead2.TYPE, READ_OPERATION_2_INTERLEAVE_KEY);
        mapping.put(ComplexRead3.TYPE, READ_OPERATION_3_INTERLEAVE_KEY);
        mapping.put(ComplexRead4.TYPE, READ_OPERATION_4_INTERLEAVE_KEY);
        mapping.put(ComplexRead5.TYPE, READ_OPERATION_5_INTERLEAVE_KEY);
        mapping.put(ComplexRead6.TYPE, READ_OPERATION_6_INTERLEAVE_KEY);
        mapping.put(ComplexRead7.TYPE, READ_OPERATION_7_INTERLEAVE_KEY);
        mapping.put(ComplexRead8.TYPE, READ_OPERATION_8_INTERLEAVE_KEY);
        mapping.put(ComplexRead9.TYPE, READ_OPERATION_9_INTERLEAVE_KEY);
        mapping.put(ComplexRead10.TYPE, READ_OPERATION_10_INTERLEAVE_KEY);
        mapping.put(ComplexRead11.TYPE, READ_OPERATION_11_INTERLEAVE_KEY);
        mapping.put(ComplexRead12.TYPE, READ_OPERATION_12_INTERLEAVE_KEY);
        mapping.put(ComplexRead13.TYPE, READ_OPERATION_13_INTERLEAVE_KEY);
        return mapping;
    }

    public final static Map<Integer, String> OPERATION_TYPE_TO_INTERLEAVE_KEY_MAPPING = typeToInterleaveKeyMapping();

    // Default value in case there is no update stream
    public final static String DEFAULT_UPDATE_INTERLEAVE = "1";

    /*
     * Operation Enable
     */
    public final static String ENABLE_SUFFIX = "_enable";
    public final static String LONG_READ_OPERATION_1_ENABLE_KEY = asEnableKey(ComplexRead1.class);
    public final static String LONG_READ_OPERATION_2_ENABLE_KEY = asEnableKey(ComplexRead2.class);
    public final static String LONG_READ_OPERATION_3_ENABLE_KEY = asEnableKey(ComplexRead3.class);
    public final static String LONG_READ_OPERATION_4_ENABLE_KEY = asEnableKey(ComplexRead4.class);
    public final static String LONG_READ_OPERATION_5_ENABLE_KEY = asEnableKey(ComplexRead5.class);
    public final static String LONG_READ_OPERATION_6_ENABLE_KEY = asEnableKey(ComplexRead6.class);
    public final static String LONG_READ_OPERATION_7_ENABLE_KEY = asEnableKey(ComplexRead7.class);
    public final static String LONG_READ_OPERATION_8_ENABLE_KEY = asEnableKey(ComplexRead8.class);
    public final static String LONG_READ_OPERATION_9_ENABLE_KEY = asEnableKey(ComplexRead9.class);
    public final static String LONG_READ_OPERATION_10_ENABLE_KEY = asEnableKey(ComplexRead10.class);
    public final static String LONG_READ_OPERATION_11_ENABLE_KEY = asEnableKey(ComplexRead11.class);
    public final static String LONG_READ_OPERATION_12_ENABLE_KEY = asEnableKey(ComplexRead12.class);
    public final static String LONG_READ_OPERATION_13_ENABLE_KEY = asEnableKey(ComplexRead13.class);
    public final static List<String> LONG_READ_OPERATION_ENABLE_KEYS = Lists.newArrayList(
            LONG_READ_OPERATION_1_ENABLE_KEY,
            LONG_READ_OPERATION_2_ENABLE_KEY,
            LONG_READ_OPERATION_3_ENABLE_KEY,
            LONG_READ_OPERATION_4_ENABLE_KEY,
            LONG_READ_OPERATION_5_ENABLE_KEY,
            LONG_READ_OPERATION_6_ENABLE_KEY,
            LONG_READ_OPERATION_7_ENABLE_KEY,
            LONG_READ_OPERATION_8_ENABLE_KEY,
            LONG_READ_OPERATION_9_ENABLE_KEY,
            LONG_READ_OPERATION_10_ENABLE_KEY,
            LONG_READ_OPERATION_11_ENABLE_KEY,
            LONG_READ_OPERATION_12_ENABLE_KEY,
            LONG_READ_OPERATION_13_ENABLE_KEY
    );
    public final static String SHORT_READ_OPERATION_1_ENABLE_KEY = asEnableKey(SimpleRead1.class);
    public final static String SHORT_READ_OPERATION_2_ENABLE_KEY = asEnableKey(SimpleRead2.class);
    public final static String SHORT_READ_OPERATION_3_ENABLE_KEY = asEnableKey(SimpleRead3.class);
    public final static String SHORT_READ_OPERATION_4_ENABLE_KEY = asEnableKey(SimpleRead4.class);
    public final static String SHORT_READ_OPERATION_5_ENABLE_KEY = asEnableKey(SimpleRead5.class);
    public final static String SHORT_READ_OPERATION_6_ENABLE_KEY = asEnableKey(SimpleRead6.class);
    public final static String SHORT_READ_OPERATION_7_ENABLE_KEY = asEnableKey(SimpleRead7.class);
    public final static String SHORT_READ_OPERATION_8_ENABLE_KEY = asEnableKey(SimpleRead8.class);
    public final static List<String> SHORT_READ_OPERATION_ENABLE_KEYS = Lists.newArrayList(
            SHORT_READ_OPERATION_1_ENABLE_KEY,
            SHORT_READ_OPERATION_2_ENABLE_KEY,
            SHORT_READ_OPERATION_3_ENABLE_KEY,
            SHORT_READ_OPERATION_4_ENABLE_KEY,
            SHORT_READ_OPERATION_5_ENABLE_KEY,
            SHORT_READ_OPERATION_6_ENABLE_KEY,
            SHORT_READ_OPERATION_7_ENABLE_KEY,
            SHORT_READ_OPERATION_8_ENABLE_KEY
    );
    public final static String WRITE_OPERATION_1_ENABLE_KEY = asEnableKey(Write1.class);
    public final static String WRITE_OPERATION_2_ENABLE_KEY = asEnableKey(Write2.class);
    public final static String WRITE_OPERATION_3_ENABLE_KEY = asEnableKey(Write3.class);
    public final static String WRITE_OPERATION_4_ENABLE_KEY = asEnableKey(Write4.class);
    public final static String WRITE_OPERATION_5_ENABLE_KEY = asEnableKey(Write5.class);
    public final static String WRITE_OPERATION_6_ENABLE_KEY = asEnableKey(Write6.class);
    public final static String WRITE_OPERATION_7_ENABLE_KEY = asEnableKey(Write7.class);
    public final static String WRITE_OPERATION_8_ENABLE_KEY = asEnableKey(Write8.class);
    public final static String WRITE_OPERATION_9_ENABLE_KEY = asEnableKey(Write9.class);
    public final static String WRITE_OPERATION_10_ENABLE_KEY = asEnableKey(Write10.class);
    public final static String WRITE_OPERATION_11_ENABLE_KEY = asEnableKey(Write11.class);
    public final static String WRITE_OPERATION_12_ENABLE_KEY = asEnableKey(Write12.class);
    public final static String WRITE_OPERATION_13_ENABLE_KEY = asEnableKey(Write13.class);
    public final static String WRITE_OPERATION_14_ENABLE_KEY = asEnableKey(Write14.class);
    public final static List<String> WRITE_OPERATION_ENABLE_KEYS = Lists.newArrayList(
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
            WRITE_OPERATION_14_ENABLE_KEY
    );

    private static String asEnableKey(Class<? extends Operation> operation) {
        return LDBC_FINBENCH_TRANSACTION_PARAM_NAME_PREFIX + operation.getSimpleName() + ENABLE_SUFFIX;
    }

    /*
     * Read Operation Parameters
     */
    public final static String READ_OPERATION_1_PARAMS_FILENAME = "transaction_1_param.txt";
    public final static String READ_OPERATION_2_PARAMS_FILENAME = "transaction_2_param.txt";
    public final static String READ_OPERATION_3_PARAMS_FILENAME = "transaction_3_param.txt";
    public final static String READ_OPERATION_4_PARAMS_FILENAME = "transaction_4_param.txt";
    public final static String READ_OPERATION_5_PARAMS_FILENAME = "transaction_5_param.txt";
    public final static String READ_OPERATION_6_PARAMS_FILENAME = "transaction_6_param.txt";
    public final static String READ_OPERATION_7_PARAMS_FILENAME = "transaction_7_param.txt";
    public final static String READ_OPERATION_8_PARAMS_FILENAME = "transaction_8_param.txt";
    public final static String READ_OPERATION_9_PARAMS_FILENAME = "transaction_9_param.txt";
    public final static String READ_OPERATION_10_PARAMS_FILENAME = "transaction_10_param.txt";
    public final static String READ_OPERATION_11_PARAMS_FILENAME = "transaction_11_param.txt";
    public final static String READ_OPERATION_12_PARAMS_FILENAME = "transaction_12_param.txt";
    public final static String READ_OPERATION_13_PARAMS_FILENAME = "transaction_13_param.txt";
    public final static List<String> READ_OPERATION_PARAMS_FILENAMES = Lists.newArrayList(
            READ_OPERATION_1_PARAMS_FILENAME,
            READ_OPERATION_2_PARAMS_FILENAME,
            READ_OPERATION_3_PARAMS_FILENAME,
            READ_OPERATION_4_PARAMS_FILENAME,
            READ_OPERATION_5_PARAMS_FILENAME,
            READ_OPERATION_6_PARAMS_FILENAME,
            READ_OPERATION_7_PARAMS_FILENAME,
            READ_OPERATION_8_PARAMS_FILENAME,
            READ_OPERATION_9_PARAMS_FILENAME,
            READ_OPERATION_10_PARAMS_FILENAME,
            READ_OPERATION_11_PARAMS_FILENAME,
            READ_OPERATION_12_PARAMS_FILENAME,
            READ_OPERATION_13_PARAMS_FILENAME
    );

    /*
     * Write Operation Parameters
     */
    public final static String PIPE_SEPARATOR_REGEX = "\\|";

    public static Map<String, String> convertFrequenciesToInterleaves(Map<String, String> params) {
        Integer updateDistance = Integer.parseInt(params.get(UPDATE_INTERLEAVE));

        Integer interleave = Integer.parseInt(params.get(READ_OPERATION_1_FREQUENCY_KEY)) * updateDistance;
        params.put(READ_OPERATION_1_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(READ_OPERATION_2_FREQUENCY_KEY)) * updateDistance;
        params.put(READ_OPERATION_2_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(READ_OPERATION_3_FREQUENCY_KEY)) * updateDistance;
        params.put(READ_OPERATION_3_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(READ_OPERATION_4_FREQUENCY_KEY)) * updateDistance;
        params.put(READ_OPERATION_4_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(READ_OPERATION_5_FREQUENCY_KEY)) * updateDistance;
        params.put(READ_OPERATION_5_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(READ_OPERATION_6_FREQUENCY_KEY)) * updateDistance;
        params.put(READ_OPERATION_6_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(READ_OPERATION_7_FREQUENCY_KEY)) * updateDistance;
        params.put(READ_OPERATION_7_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(READ_OPERATION_8_FREQUENCY_KEY)) * updateDistance;
        params.put(READ_OPERATION_8_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(READ_OPERATION_9_FREQUENCY_KEY)) * updateDistance;
        params.put(READ_OPERATION_9_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(READ_OPERATION_10_FREQUENCY_KEY)) * updateDistance;
        params.put(READ_OPERATION_10_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(READ_OPERATION_11_FREQUENCY_KEY)) * updateDistance;
        params.put(READ_OPERATION_11_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(READ_OPERATION_12_FREQUENCY_KEY)) * updateDistance;
        params.put(READ_OPERATION_12_INTERLEAVE_KEY, interleave.toString());

        interleave = Integer.parseInt(params.get(READ_OPERATION_13_FREQUENCY_KEY)) * updateDistance;
        params.put(READ_OPERATION_13_INTERLEAVE_KEY, interleave.toString());

        return params;
    }

    public static Map<String, String> defaultConfigSF1() throws IOException {
        String filename = "/configuration/ldbc/finbench/transaction/sf_internal_test.properties";
        return ConsoleAndFileDriverConfiguration.convertLongKeysToShortKeys(resourceToMap(filename));
    }

    private static Map<String, String> resourceToMap(String filename) throws IOException {
        try (InputStream inputStream = LdbcFinBenchTransactionWorkloadConfiguration.class.getResource(filename).openStream()) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return new HashMap<>(Maps.fromProperties(properties));
        }
    }

    public static Map<String, String> defaultReadOnlyConfigSF1() throws DriverConfigurationException, IOException {
        Map<String, String> params = withoutWrites(
                defaultConfigSF1()
        );
        return ConsoleAndFileDriverConfiguration.convertLongKeysToShortKeys(params);
    }

    public static Map<String, String> withOnly(
            Map<String, String> originalParams,
            Class<? extends Operation>... operationClasses)
            throws DriverConfigurationException, IOException {
        Map<String, String> params = withoutWrites(
                withoutShortReads(
                        withoutLongReads(originalParams)
                )
        );
        for (Class<? extends Operation> operationClass : operationClasses) {
            params.put(asEnableKey(operationClass), "true");
        }
        return ConsoleAndFileDriverConfiguration.convertLongKeysToShortKeys(params);
    }

    public static boolean hasReads(Map<String, String> params) {
        return Lists.newArrayList(
                LONG_READ_OPERATION_1_ENABLE_KEY,
                LONG_READ_OPERATION_2_ENABLE_KEY,
                LONG_READ_OPERATION_3_ENABLE_KEY,
                LONG_READ_OPERATION_4_ENABLE_KEY,
                LONG_READ_OPERATION_5_ENABLE_KEY,
                LONG_READ_OPERATION_6_ENABLE_KEY,
                LONG_READ_OPERATION_7_ENABLE_KEY,
                LONG_READ_OPERATION_8_ENABLE_KEY,
                LONG_READ_OPERATION_9_ENABLE_KEY,
                LONG_READ_OPERATION_10_ENABLE_KEY,
                LONG_READ_OPERATION_11_ENABLE_KEY,
                LONG_READ_OPERATION_12_ENABLE_KEY,
                LONG_READ_OPERATION_13_ENABLE_KEY).stream().anyMatch(key -> isSet(params, key));
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
        return params.containsKey(key) &&
                null != params.get(key) &&
                Boolean.parseBoolean(params.get(key));
    }

    public static Map<String, String> withoutShortReads(Map<String, String> originalParams)
            throws DriverConfigurationException, IOException {
        Map<String, String> params = MapUtils.copyExcludingKeys(originalParams, new HashSet<>());
        params.put(SHORT_READ_OPERATION_1_ENABLE_KEY, "false");
        params.put(SHORT_READ_OPERATION_2_ENABLE_KEY, "false");
        params.put(SHORT_READ_OPERATION_3_ENABLE_KEY, "false");
        params.put(SHORT_READ_OPERATION_4_ENABLE_KEY, "false");
        params.put(SHORT_READ_OPERATION_5_ENABLE_KEY, "false");
        params.put(SHORT_READ_OPERATION_6_ENABLE_KEY, "false");
        params.put(SHORT_READ_OPERATION_7_ENABLE_KEY, "false");
        return ConsoleAndFileDriverConfiguration.convertLongKeysToShortKeys(params);
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
        return ConsoleAndFileDriverConfiguration.convertLongKeysToShortKeys(params);
    }

    public static Map<String, String> withoutLongReads(Map<String, String> originalParams)
            throws DriverConfigurationException, IOException {
        Map<String, String> params = MapUtils.copyExcludingKeys(originalParams, new HashSet<String>());
        params.put(LONG_READ_OPERATION_1_ENABLE_KEY, "false");
        params.put(LONG_READ_OPERATION_2_ENABLE_KEY, "false");
        params.put(LONG_READ_OPERATION_3_ENABLE_KEY, "false");
        params.put(LONG_READ_OPERATION_4_ENABLE_KEY, "false");
        params.put(LONG_READ_OPERATION_5_ENABLE_KEY, "false");
        params.put(LONG_READ_OPERATION_6_ENABLE_KEY, "false");
        params.put(LONG_READ_OPERATION_7_ENABLE_KEY, "false");
        params.put(LONG_READ_OPERATION_8_ENABLE_KEY, "false");
        params.put(LONG_READ_OPERATION_9_ENABLE_KEY, "false");
        params.put(LONG_READ_OPERATION_10_ENABLE_KEY, "false");
        params.put(LONG_READ_OPERATION_11_ENABLE_KEY, "false");
        params.put(LONG_READ_OPERATION_12_ENABLE_KEY, "false");
        params.put(LONG_READ_OPERATION_13_ENABLE_KEY, "false");
        params.put(SHORT_READ_OPERATION_1_ENABLE_KEY, "false");
        params.put(SHORT_READ_OPERATION_2_ENABLE_KEY, "false");
        params.put(SHORT_READ_OPERATION_3_ENABLE_KEY, "false");
        params.put(SHORT_READ_OPERATION_4_ENABLE_KEY, "false");
        params.put(SHORT_READ_OPERATION_5_ENABLE_KEY, "false");
        params.put(SHORT_READ_OPERATION_6_ENABLE_KEY, "false");
        params.put(SHORT_READ_OPERATION_7_ENABLE_KEY, "false");
        return ConsoleAndFileDriverConfiguration.convertLongKeysToShortKeys(params);
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
        operationTypeToClassMapping.put(ComplexRead13.TYPE, ComplexRead13.class);
        operationTypeToClassMapping.put(SimpleRead1.TYPE, SimpleRead1.class);
        operationTypeToClassMapping.put(SimpleRead2.TYPE, SimpleRead2.class);
        operationTypeToClassMapping.put(SimpleRead3.TYPE, SimpleRead3.class);
        operationTypeToClassMapping.put(SimpleRead4.TYPE, SimpleRead4.class);
        operationTypeToClassMapping.put(SimpleRead5.TYPE, SimpleRead5.class);
        operationTypeToClassMapping.put(SimpleRead6.TYPE, SimpleRead6.class);
        operationTypeToClassMapping.put(SimpleRead7.TYPE, SimpleRead7.class);
        operationTypeToClassMapping.put(SimpleRead8.TYPE, SimpleRead8.class);
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
        return operationTypeToClassMapping;
    }

    static String removeSuffix(String original, String suffix) {
        return (!original.contains(suffix)) ? original : original.substring(0, original.lastIndexOf(suffix));
    }

    static String removePrefix(String original, String prefix) {
        return (!original.contains(prefix)) ? original : original
                .substring(original.lastIndexOf(prefix) + prefix.length(), original.length());
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
}
