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

package org.ldbcouncil.finbench.driver.workloads.simple;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.WorkloadStreams;
import org.ldbcouncil.finbench.driver.generator.GeneratorFactory;

public class SimpleWorkload extends Workload {
    @Override
    public Map<Integer, Class<? extends Operation>> operationTypeToClassMapping() {
        return null;
    }

    @Override
    public void onInit(Map<String, String> params) throws WorkloadException {

    }

    @Override
    public Class<? extends Operation> getOperationClass() {
        return null;
    }

    @Override
    protected void onClose() throws IOException {

    }

    @Override
    protected WorkloadStreams getStreams(GeneratorFactory generators, boolean hasDbConnected) throws WorkloadException {
        return null;
    }

    @Override
    public Set<Class> enabledValidationOperations() {
        return null;
    }
}
