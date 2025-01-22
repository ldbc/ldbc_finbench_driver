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

public class MultiWriterCompletionTimeStateManagerWriter implements CompletionTimeWriter {
    private final int id;
    private final MultiWriterCompletionTimeStateManager completionTimeStateManager;

    MultiWriterCompletionTimeStateManagerWriter(
        int id,
        MultiWriterCompletionTimeStateManager completionTimeStateManager) {
        this.id = id;
        this.completionTimeStateManager = completionTimeStateManager;
    }

    @Override
    public void submitInitiatedTime(long timeAsMilli) throws CompletionTimeException {
        completionTimeStateManager.submitInitiatedTime(id, timeAsMilli);
    }

    @Override
    public void submitCompletedTime(long timeAsMilli) throws CompletionTimeException {
        completionTimeStateManager.submitCompletedTime(id, timeAsMilli);
    }

    int id() {
        return id;
    }

    @Override
    public String toString() {
        return "MultiWriterCompletionTimeStateManagerWriter{" + "id=" + id + '}';
    }
}
