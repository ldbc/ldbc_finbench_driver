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

package org.ldbcouncil.finbench.driver.runtime.coordination;

import java.util.List;
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;
import org.ldbcouncil.finbench.driver.runtime.scheduling.Spinner;
import org.ldbcouncil.finbench.driver.temporal.TimeSource;

public class CompletionTimeServiceAssistant {
    public void writeInitiatedAndCompletedTimesToAllWriters(
        CompletionTimeService completionTimeService,
        long timeAsMilli) throws CompletionTimeException {
        List<CompletionTimeWriter> writers = completionTimeService.getAllWriters();
        for (CompletionTimeWriter writer : writers) {
            writer.submitInitiatedTime(timeAsMilli);
            writer.submitCompletedTime(timeAsMilli);
        }
    }

    public boolean waitForCompletionTime(
        TimeSource timeSource,
        long completionTimeToWaitForAsMilli,
        long timeoutDurationAsMilli,
        CompletionTimeService completionTimeService,
        ConcurrentErrorReporter errorReporter) throws CompletionTimeException {
        long sleepDurationAsMilli = 100;
        long timeoutTimeAsMilli = timeSource.nowAsMilli() + timeoutDurationAsMilli;
        while (timeSource.nowAsMilli() < timeoutTimeAsMilli) {
            long currentCompletionTimeAsMilli = completionTimeService.completionTimeAsMilli();
            if (-1 == currentCompletionTimeAsMilli) {
                continue;
            }
            if (completionTimeToWaitForAsMilli <= currentCompletionTimeAsMilli) {
                return true;
            }
            if (errorReporter.errorEncountered()) {
                throw new CompletionTimeException("Encountered error while waiting for CT");
            }
            Spinner.powerNap(sleepDurationAsMilli);
        }
        return false;
    }

    public SynchronizedCompletionTimeService newSynchronizedCompletionTimeService() throws CompletionTimeException {
        return new SynchronizedCompletionTimeService();
    }

    public ThreadedQueuedCompletionTimeService newThreadedQueuedCompletionTimeService(
        TimeSource timeSource,
        ConcurrentErrorReporter errorReporter) throws CompletionTimeException {
        return new ThreadedQueuedCompletionTimeService(timeSource, errorReporter);
    }
}
