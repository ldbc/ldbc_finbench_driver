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
