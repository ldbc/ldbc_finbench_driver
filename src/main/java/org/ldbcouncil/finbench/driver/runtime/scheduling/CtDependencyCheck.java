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

package org.ldbcouncil.finbench.driver.runtime.scheduling;

import static java.lang.String.format;

import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeException;
import org.ldbcouncil.finbench.driver.runtime.coordination.CompletionTimeReader;
import org.ldbcouncil.finbench.driver.temporal.TemporalUtil;

public class CtDependencyCheck implements SpinnerCheck {
    private static final TemporalUtil TEMPORAL_UTIL = new TemporalUtil();
    private final CompletionTimeReader completionTimeReader;
    private final ConcurrentErrorReporter errorReporter;

    public CtDependencyCheck(CompletionTimeReader completionTimeReader, ConcurrentErrorReporter errorReporter) {
        this.completionTimeReader = completionTimeReader;
        this.errorReporter = errorReporter;
    }

    @Override
    public SpinnerCheckResult doCheck(Operation operation) {
        try {
            return (completionTimeReader.completionTimeAsMilli() >= operation.dependencyTimeStamp())
                ? SpinnerCheckResult.PASSED : SpinnerCheckResult.STILL_CHECKING;
        } catch (CompletionTimeException e) {
            errorReporter.reportError(this,
                format(
                    "Error encountered while reading CT for query %s\n%s",
                    operation.getClass().getSimpleName(),
                    ConcurrentErrorReporter.stackTraceToString(e)));
            return SpinnerCheckResult.FAILED;
        }
    }

    @Override
    public boolean handleFailedCheck(Operation operation) {
        try {
            // Note, CT printed here may be a little later than CT that was measured during check
            errorReporter.reportError(this,
                format("CT(%s) has not advanced sufficiently to execute operation\n"
                        + "Operation: %s\n"
                        + "Time Stamp: %s\n"
                        + "Dependency Time Stamp: %s",
                    TEMPORAL_UTIL.milliTimeToDateTimeString(completionTimeReader.completionTimeAsMilli()),
                    operation,
                    operation.timeStamp(),
                    operation.dependencyTimeStamp()));
            return false;
        } catch (CompletionTimeException e) {
            errorReporter.reportError(this,
                format(
                    "Error encountered in handleFailedCheck while reading CT for query %s\n%s",
                    operation.getClass().getSimpleName(),
                    ConcurrentErrorReporter.stackTraceToString(e)));
            return false;
        }
    }
}
