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
