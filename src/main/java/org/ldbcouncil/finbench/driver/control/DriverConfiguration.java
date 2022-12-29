package org.ldbcouncil.finbench.driver.control;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface DriverConfiguration {
    String mode();

    String name();

    String dbClassName();

    String workloadClassName();

    long operationCount();

    int threadCount();

    int statusDisplayIntervalAsSeconds();

    TimeUnit timeUnit();

    String resultDirPath();

    double timeCompressionRatio();

    int validationParametersSize();

    boolean validationSerializationCheck();

    boolean recordDelayedOperations();

    String databaseValidationFilePath();

    long spinnerSleepDurationAsMilli();

    boolean shouldPrintHelpString();

    String helpString();

    boolean ignoreScheduledStartTimes();

    long warmupCount();

    long skipCount();

    boolean flushLog();

    String toPropertiesString() throws DriverConfigurationException;

    Map<String, String> asMap();

    DriverConfiguration applyArgs(DriverConfiguration newConfiguration) throws DriverConfigurationException;

    DriverConfiguration applyArgs(Map<String, String> newMap) throws DriverConfigurationException;

    DriverConfiguration applyArg(String argument, String newValue) throws DriverConfigurationException;
}
