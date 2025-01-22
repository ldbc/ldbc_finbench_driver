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

package org.ldbcouncil.finbench.driver.workloads;

import static java.lang.String.format;

import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.util.ClassLoaderHelper;

public class ClassNameWorkloadFactory implements WorkloadFactory {
    private final String workloadClassName;

    public ClassNameWorkloadFactory(String workloadClassName) {
        this.workloadClassName = workloadClassName;
    }

    public Workload createWorkload() throws WorkloadException {
        try {
            return ClassLoaderHelper.loadWorkload(workloadClassName);
        } catch (Exception e) {
            throw new WorkloadException(format("Error loading Workload class: %s", workloadClassName), e);
        }
    }
}
