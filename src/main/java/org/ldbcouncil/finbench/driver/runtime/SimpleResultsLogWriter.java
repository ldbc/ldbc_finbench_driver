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

package org.ldbcouncil.finbench.driver.runtime;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.ldbcouncil.finbench.driver.csv.simple.SimpleCsvFileWriter;
import org.ldbcouncil.finbench.driver.runtime.metrics.ResultsLogWriter;

public class SimpleResultsLogWriter implements ResultsLogWriter {
    private final SimpleCsvFileWriter writer;
    private final TimeUnit unit;

    public SimpleResultsLogWriter(File resultsLog, TimeUnit unit, boolean flushLog) throws IOException {
        this.writer = new SimpleCsvFileWriter(resultsLog, SimpleCsvFileWriter.DEFAULT_COLUMN_SEPARATOR, flushLog);
        this.unit = unit;
        resultsLog.createNewFile();
        writer.writeRow(
            HEADER_OPERATION_TYPE,
            HEADER_SCHEDULED_START_TIME,
            HEADER_ACTUAL_START_TIME,
            HEADER_EXECUTION_DURATION_PREFIX + unit.name(),
            HEADER_RESULT_CODE,
            HEADER_ORIGINAL_START_TIME
        );
    }

    @Override
    public void write(
        String operationName,
        long scheduledStartTimeAsMilli,
        long actualStartTimeAsMilli,
        long runDurationAsNano,
        int resultCode,
        long originalStartTime) throws IOException {
        writer.writeRow(
            operationName,
            Long.toString(scheduledStartTimeAsMilli),
            Long.toString(actualStartTimeAsMilli),
            Long.toString(unit.convert(runDurationAsNano, TimeUnit.NANOSECONDS)),
            Integer.toString(resultCode),
            Long.toString(originalStartTime)
        );
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }
}
