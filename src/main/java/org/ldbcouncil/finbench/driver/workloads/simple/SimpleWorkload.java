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
