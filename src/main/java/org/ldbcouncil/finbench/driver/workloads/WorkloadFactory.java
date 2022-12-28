package org.ldbcouncil.finbench.driver.workloads;

import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.WorkloadException;

public interface WorkloadFactory {
    Workload createWorkload() throws WorkloadException;
}
