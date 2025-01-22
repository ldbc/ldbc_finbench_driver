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

package org.ldbcouncil.finbench.driver.workloads.dummy;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.WorkloadStreams;
import org.ldbcouncil.finbench.driver.workloads.WorkloadFactory;

public class DummyWorkloadFactory implements WorkloadFactory {
    private final Iterator<WorkloadStreams> streams;
    private final Iterator<Operation> alternativeLastOperations;
    private final long maxExpectedInterleaveAsMilli;

    public DummyWorkloadFactory(Iterator<WorkloadStreams> streams,
                                long maxExpectedInterleaveAsMilli) {
        this(streams, null, maxExpectedInterleaveAsMilli);
    }

    public DummyWorkloadFactory(Iterator<WorkloadStreams> streams,
                                Iterator<Operation> alternativeLastOperations,
                                long maxExpectedInterleaveAsMilli) {
        this.streams = streams;
        this.alternativeLastOperations = alternativeLastOperations;
        this.maxExpectedInterleaveAsMilli = maxExpectedInterleaveAsMilli;
    }

    @Override
    public Workload createWorkload() throws WorkloadException {
        WorkloadStreams workloadStreams;
        if (null == alternativeLastOperations) {
            workloadStreams = streams.next();
        } else {
            workloadStreams = streams.next();
            List<Operation>
                asynchronousNonDependencyOperationsToReturn =
                Lists.newArrayList(workloadStreams.asynchronousStream().nonDependencyOperations());
            asynchronousNonDependencyOperationsToReturn.remove(asynchronousNonDependencyOperationsToReturn.size() - 1);
            asynchronousNonDependencyOperationsToReturn.add(alternativeLastOperations.next());
            workloadStreams.setAsynchronousStream(
                workloadStreams.asynchronousStream().dependentOperationTypes(),
                workloadStreams.asynchronousStream().dependencyOperationTypes(),
                workloadStreams.asynchronousStream().dependencyOperations(),
                asynchronousNonDependencyOperationsToReturn.iterator(),
                workloadStreams.asynchronousStream().childOperationGenerator()
            );
        }
        return new DummyWorkload(workloadStreams, maxExpectedInterleaveAsMilli);
    }
}
