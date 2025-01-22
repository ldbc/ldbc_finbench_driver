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

package org.ldbcouncil.finbench.driver.runtime.metrics;

import java.io.IOException;

public interface ResultsLogWriter extends AutoCloseable {
    String HEADER_OPERATION_TYPE = "operation_type";
    String HEADER_SCHEDULED_START_TIME = "scheduled_start_time";
    String HEADER_ACTUAL_START_TIME = "actual_start_time";
    String HEADER_EXECUTION_DURATION_PREFIX = "execution_duration_";
    String HEADER_RESULT_CODE = "result_code";
    String HEADER_ORIGINAL_START_TIME = "original_start_time";

    int INDEX_OPERATION_TYPE = 0;
    int INDEX_SCHEDULED_START_TIME = 1;
    int INDEX_ACTUAL_START_TIME = 2;
    int INDEX_EXECUTION_DURATION = 3;
    int INDEX_RESULT_CODE = 4;
    int INDEX_ORIGINAL_START_TIME = 5;

    void write(
        String operationName,
        long scheduledStartTimeAsMilli,
        long actualStartTimeAsMilli,
        long runDurationAsNano,
        int resultCode,
        long originalStartTime) throws IOException;
}
