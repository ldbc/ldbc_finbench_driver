package org.ldbcouncil.finbench.driver.driver;

import static java.util.stream.Collectors.joining;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.ldbcouncil.finbench.driver.control.ConsoleAndFileDriverConfiguration;
import org.ldbcouncil.finbench.driver.control.DriverConfiguration;
import org.ldbcouncil.finbench.driver.control.DriverConfigurationException;
import org.ldbcouncil.finbench.driver.csv.simple.SimpleCsvFileReader;
import org.ldbcouncil.finbench.driver.util.FileUtils;
import org.ldbcouncil.finbench.driver.util.MapUtils;

public class ResultsDirectory {
    public enum BenchmarkPhase {
        NOT_FOUND,
        INITIALIZING,
        WARMUP,
        WARMUP_FINISHED,
        MEASUREMENT,
        MEASUREMENT_FINISHED
    }

    private static final String WARMUP_IDENTIFIER = "-WARMUP-";

    private static final String RESULTS_LOG_FILENAME_SUFFIX = "-results_log.csv";
    private static final String RESULTS_METRICS_FILENAME_SUFFIX = "-results.json";
    private static final String RESULTS_CONFIGURATION_FILENAME_SUFFIX = "-configuration.properties";

    private static final String RESULTS_VALIDATION_FILENAME_SUFFIX = "-validation.json";

    private final DriverConfiguration configuration;
    private final File resultsDir;

    public static ResultsDirectory fromDirectory(File resultsDir)
        throws IOException, DriverConfigurationException, DriverException {
        File configurationFile = findConfigurationFile(resultsDir, false);
        if (null == configurationFile) {
            throw new RuntimeException("Could not find configuration file in: " + resultsDir.getAbsolutePath());
        }
        return new ResultsDirectory(getConfigurationFrom(configurationFile));
    }

    public ResultsDirectory(DriverConfiguration configuration) throws DriverException {
        this.configuration = configuration;
        if (null == configuration.resultDirPath()) {
            this.resultsDir = null;
        } else {
            this.resultsDir = new File(configuration.resultDirPath());
            if (this.resultsDir.exists() && !this.resultsDir.isDirectory()) {
                throw new DriverException("Results directory is not directory: " + this.resultsDir.getAbsolutePath());
            } else if (!this.resultsDir.exists()) {
                try {
                    FileUtils.tryCreateDirs(this.resultsDir, false);
                } catch (Exception e) {
                    throw new DriverException(
                        "Results directory could not be created: " + this.resultsDir.getAbsolutePath(), e);
                }
            }
        }
    }

    public boolean exists() {
        return null != resultsDir;
    }

    private static boolean exists(File resultsDir) {
        return resultsDir != null && resultsDir.isDirectory() && resultsDir.exists();
    }

    public File getResultsLogFile(boolean warmup) throws DriverException {
        return getResultsLogFile(resultsDir, configuration, warmup);
    }

    private static File getResultsLogFile(File resultsDir, DriverConfiguration configuration, boolean warmup)
        throws DriverException {
        return new File(resultsDir, resultsLogFilename(configuration, warmup));
    }

    File getOrCreateResultsLogFile(boolean warmup) throws DriverException {
        File resultsLog = getResultsLogFile(resultsDir, configuration, warmup);
        if (!resultsLog.exists()) {
            try {
                FileUtils.createOrFail(resultsLog);
            } catch (IOException e) {
                throw new DriverException("Error creating results log file: " + resultsLog.getAbsolutePath(), e);
            }
        }
        return resultsLog;
    }

    public long getResultsLogFileLength(boolean warmup) throws DriverException {
        try (SimpleCsvFileReader csvResultsLogReader = new SimpleCsvFileReader(
            getResultsLogFile(resultsDir, configuration, warmup),
            SimpleCsvFileReader.DEFAULT_COLUMN_SEPARATOR_REGEX_STRING)) {
            return Iterators.size(csvResultsLogReader);
        } catch (FileNotFoundException e) {
            throw new DriverException(
                "Error calculating length of " + getResultsLogFile(warmup).getAbsolutePath(), e);
        }
    }

    public File getOrCreateResultsSummaryFile(boolean warmup) throws DriverException {
        File resultsSummary = getResultsSummaryFile(warmup);
        if (!resultsSummary.exists()) {
            try {
                FileUtils.createOrFail(resultsSummary);
            } catch (IOException e) {
                throw new DriverException(
                    "Error creating results summary file: " + resultsSummary.getAbsolutePath(), e);
            }
        }
        return resultsSummary;
    }

    private static File getResultsSummaryFile(File resultsDir, DriverConfiguration configuration, boolean warmup)
        throws DriverException {
        return new File(resultsDir, resultsSummaryFilename(configuration, warmup));
    }

    private File getResultsSummaryFile(boolean warmup) throws DriverException {
        return getResultsSummaryFile(resultsDir, configuration, warmup);
    }

    File getOrCreateConfigurationFile(boolean warmup) throws DriverException {
        File configurationFile = getConfigurationFile(warmup);
        if (!configurationFile.exists()) {
            try {
                FileUtils.createOrFail(configurationFile);
            } catch (IOException e) {
                throw new DriverException(
                    "Error creating configuration file: " + configurationFile.getAbsolutePath(), e);
            }
        }
        return configurationFile;
    }

    private File getConfigurationFile(boolean warmup) throws DriverException {
        return new File(resultsDir, configurationFilename(configuration, warmup));
    }

    File getOrCreateResultsValidationFile(boolean warmup) throws DriverException {
        File resultsValidationFile = getResultsValidationFile(warmup);
        if (!resultsValidationFile.exists()) {
            try {
                FileUtils.createOrFail(resultsValidationFile);
            } catch (IOException e) {
                throw new DriverException(
                    "Error creating results validation file: " + resultsValidationFile.getAbsolutePath(), e);
            }
        }
        return resultsValidationFile;
    }

    private File getResultsValidationFile(boolean warmup) throws DriverException {
        return new File(resultsDir, resultsValidationFilename(configuration, warmup));
    }

    public Set<File> files() throws DriverException {
        return Sets.newHashSet(resultsDir.listFiles());
    }

    public Set<File> expectedFiles() throws DriverException {
        if (null == resultsDir) {
            return Collections.emptySet();
        } else {
            Set<File> expectedFiles = new HashSet<>();
            if (configuration.warmupCount() > 0) {
                if (!configuration.ignoreScheduledStartTimes()) {
                    expectedFiles.add(getResultsValidationFile(true));
                }
                expectedFiles.add(getResultsLogFile(true));
                expectedFiles.add(getResultsSummaryFile(true));
                expectedFiles.add(getConfigurationFile(true));
            }
            if (!configuration.ignoreScheduledStartTimes()) {
                expectedFiles.add(getResultsValidationFile(false));
            }
            expectedFiles.add(getResultsLogFile(false));
            expectedFiles.add(getResultsSummaryFile(false));
            expectedFiles.add(getConfigurationFile(false));
            return expectedFiles;
        }
    }

    public static BenchmarkPhase phase(File resultsDir)
        throws DriverException, DriverConfigurationException, IOException {
        if (!exists(resultsDir)) {
            // Results directory has not yet been created
            return BenchmarkPhase.NOT_FOUND;
        }

        File warmupResultsLog = findResultsLogFile(resultsDir, true);
        if (null == warmupResultsLog || !warmupResultsLog.exists()) {
            // Warmup results log (the first file to be created during warmup) has not yet been created
            return BenchmarkPhase.INITIALIZING;
        }

        // Warmup results log exists. Warmup has started
        File warmupConfigurationFile = findConfigurationFile(resultsDir, true);
        if (null == warmupConfigurationFile) {
            // Warmup results log is present, but warmup configuration file is not. Warmup is still running
            return BenchmarkPhase.WARMUP;
        }

        // Warmup configuration file exists
        DriverConfiguration warmupConfiguration = getConfigurationFrom(warmupConfigurationFile);
        File warmupSummary = getResultsSummaryFile(resultsDir, warmupConfiguration, true);
        if (!warmupSummary.exists()) {
            // Warmup configuration file is present, but warmup summary file is not. Warmup is still running
            return BenchmarkPhase.WARMUP;
        }

        // Warmup summary exists. Warmup as finished
        File measurementResultsLogFile = findResultsLogFile(resultsDir, false);
        if (null == measurementResultsLogFile || !measurementResultsLogFile.exists()) {
            // Warmup summary is present, but measurement results log is not. Waiting for measurement to start
            return BenchmarkPhase.WARMUP_FINISHED;
        }

        // Measurement results log exists. Measurement has started
        File measurementSummary = getResultsSummaryFile(resultsDir, warmupConfiguration, false);
        if (!measurementSummary.exists()) {
            // Measurement results log is present, but measurement summary file is not. Measurement is still running
            return BenchmarkPhase.MEASUREMENT;
        }

        // Measurement summary file is present. We're done
        return BenchmarkPhase.MEASUREMENT_FINISHED;
    }

    private static File findConfigurationFile(File resultsDir, boolean warmup)
        throws DriverConfigurationException, IOException {
        FileFilter configurationFileFilter = file ->
            file.getName().contains(WARMUP_IDENTIFIER) == warmup
                && file.getName().endsWith(RESULTS_CONFIGURATION_FILENAME_SUFFIX);
        File[] resultFiles = resultsDir.listFiles(configurationFileFilter);
        if (null == resultFiles || resultFiles.length != 1) {
            return null;
        } else {
            return resultFiles[0];
        }
    }

    private static DriverConfiguration getConfigurationFrom(File configurationFile)
        throws DriverConfigurationException, IOException {
        Map<String, String> configurationMap = MapUtils.loadPropertiesToMap(configurationFile);
        return ConsoleAndFileDriverConfiguration.fromParamsMap(configurationMap);
    }

    private static File findResultsLogFile(File resultsDir, boolean warmup)
        throws DriverConfigurationException, IOException {
        FileFilter resultsLogFileFilter = file ->
            file.getName().contains(WARMUP_IDENTIFIER) == warmup
                && file.getName().endsWith(RESULTS_LOG_FILENAME_SUFFIX);
        File[] resultFiles = resultsDir.listFiles(resultsLogFileFilter);
        if (null == resultFiles || resultFiles.length == 0) {
            return null;
        } else if (resultFiles.length > 1) {
            throw new RuntimeException(
                "Expected to find 1 results log file, but found: "
                    + Arrays.stream(resultFiles).map(File::getAbsolutePath).collect(joining(",")));
        } else {
            return resultFiles[0];
        }
    }

    private static String resultsValidationFilename(DriverConfiguration configuration, boolean warmup) {
        return (warmup) ? configuration.name() + WARMUP_IDENTIFIER + RESULTS_VALIDATION_FILENAME_SUFFIX
            : configuration.name() + RESULTS_VALIDATION_FILENAME_SUFFIX;
    }

    private static String resultsLogFilename(DriverConfiguration configuration, boolean warmup) {
        return (warmup) ? configuration.name() + WARMUP_IDENTIFIER + RESULTS_LOG_FILENAME_SUFFIX
            : configuration.name() + RESULTS_LOG_FILENAME_SUFFIX;
    }

    private static String resultsSummaryFilename(DriverConfiguration configuration, boolean warmup) {
        return (warmup) ? configuration.name() + WARMUP_IDENTIFIER + RESULTS_METRICS_FILENAME_SUFFIX
            : configuration.name() + RESULTS_METRICS_FILENAME_SUFFIX;
    }

    private static String configurationFilename(DriverConfiguration configuration, boolean warmup) {
        return (warmup) ? configuration.name() + WARMUP_IDENTIFIER + RESULTS_CONFIGURATION_FILENAME_SUFFIX
            : configuration.name() + RESULTS_CONFIGURATION_FILENAME_SUFFIX;
    }
}
