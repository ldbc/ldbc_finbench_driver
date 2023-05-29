package org.ldbcouncil.finbench.driver.workloads.transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.Workload.DbValidationParametersFilter;
import org.ldbcouncil.finbench.driver.Workload.DbValidationParametersFilterAcceptance;
import org.ldbcouncil.finbench.driver.Workload.DbValidationParametersFilterResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead6;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead7;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead9;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead6;

class LdbcFinBenchTransactionDbValidationParametersFilter implements DbValidationParametersFilter {
    private final Map<Class, Long> remainingRequiredResultsPerType;
    private final Set<Class<? extends Operation>> enabledShortReadOperationTypes;

    LdbcFinBenchTransactionDbValidationParametersFilter(
        Map<Class, Long> remainingRequiredResultsPerType,
        Set<Class<? extends Operation>> enabledShortReadOperationTypes) {
        this.remainingRequiredResultsPerType = remainingRequiredResultsPerType;
        this.enabledShortReadOperationTypes = enabledShortReadOperationTypes;
    }

    /**
     * Check if the given operation is enabled or disabled
     * or that extra params needs to be generated.
     *
     * @param operation The operation to evaluate
     */
    @Override
    public boolean useOperation(Operation operation) {
        Class operationType = operation.getClass();

        if (enabledShortReadOperationTypes.contains(operationType)) {
            // Always generate short read operations after updates for validation
            return true;
        }

        if (remainingRequiredResultsPerType.containsKey(operationType)) {
            return remainingRequiredResultsPerType.get(operationType) > 0;
        } else {
            return false;
        }
    }

    @Override
    public DbValidationParametersFilterResult useOperationAndResultForValidation(
        Operation operation,
        Object operationResult
    ) {
        Class operationType = operation.getClass();
        List<Operation> injectedOperations = new ArrayList<>();

        injectedOperations.addAll(generateOperationsToInject(operation));

        if (remainingRequiredResultsPerType.containsKey(operationType)) {
            // decrement count for write operation type
            remainingRequiredResultsPerType
                .put(operationType, Math.max(0, remainingRequiredResultsPerType.get(operationType) - 1));
        } else {
            throw new RuntimeException("Unexpected operation type: " + operationType.getSimpleName());
        }

        if (haveCompletedAllRequiredResultsPerOperationType(remainingRequiredResultsPerType)) {
            return new DbValidationParametersFilterResult(DbValidationParametersFilterAcceptance.ACCEPT_AND_FINISH,
                injectedOperations);
        } else {
            return new DbValidationParametersFilterResult(DbValidationParametersFilterAcceptance.ACCEPT_AND_CONTINUE,
                injectedOperations);
        }
    }

    private boolean haveCompletedAllRequiredResultsPerOperationType(Map<Class, Long> requiredResultsPerOperationType) {
        for (Long value : requiredResultsPerOperationType.values()) {
            if (value > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Inject simple reads after a LdbcUpdate operation is scheduled.
     *
     * @param operation The update operation
     * @return List of short reads operations
     */
    private List<Operation> generateOperationsToInject(Operation operation) {
        List<Operation> operationsToInject = new ArrayList<>();
        // The input from ComplexRead as the input to SimpleRead (only Account type)
        switch (operation.type()) {
            case ComplexRead1.TYPE: {
                ComplexRead1 updateOperation = (ComplexRead1) operation;
                injectAccountSimples(operationsToInject,
                    updateOperation.getId(),
                    LdbcFinBenchSimpleReadGenerator.THRESHOLD,
                    updateOperation.getStartTime(),
                    updateOperation.getEndTime());
                break;
            }
            case ComplexRead3.TYPE: {
                ComplexRead3 updateOperation = (ComplexRead3) operation;
                injectAccountSimples(operationsToInject,
                    updateOperation.getId1(),
                    LdbcFinBenchSimpleReadGenerator.THRESHOLD,
                    updateOperation.getStartTime(),
                    updateOperation.getEndTime());
                injectAccountSimples(operationsToInject,
                    updateOperation.getId2(),
                    LdbcFinBenchSimpleReadGenerator.THRESHOLD,
                    updateOperation.getStartTime(),
                    updateOperation.getEndTime());
                break;
            }
            case ComplexRead4.TYPE: {
                ComplexRead4 updateOperation = (ComplexRead4) operation;
                injectAccountSimples(operationsToInject,
                    updateOperation.getId1(),
                    LdbcFinBenchSimpleReadGenerator.THRESHOLD,
                    updateOperation.getStartTime(),
                    updateOperation.getEndTime());
                injectAccountSimples(operationsToInject,
                    updateOperation.getId2(),
                    LdbcFinBenchSimpleReadGenerator.THRESHOLD,
                    updateOperation.getStartTime(),
                    updateOperation.getEndTime());
                break;
            }
            case ComplexRead6.TYPE: {
                ComplexRead6 updateOperation = (ComplexRead6) operation;
                injectAccountSimples(operationsToInject,
                    updateOperation.getId(),
                    updateOperation.getThreshold1(),
                    updateOperation.getStartTime(),
                    updateOperation.getEndTime());
                break;
            }
            case ComplexRead7.TYPE: {
                ComplexRead7 updateOperation = (ComplexRead7) operation;
                injectAccountSimples(operationsToInject,
                    updateOperation.getId(),
                    updateOperation.getThreshold(),
                    updateOperation.getStartTime(),
                    updateOperation.getEndTime());
                break;
            }
            case ComplexRead9.TYPE: {
                ComplexRead9 updateOperation = (ComplexRead9) operation;
                injectAccountSimples(operationsToInject,
                    updateOperation.getId(),
                    updateOperation.getThreshold(),
                    updateOperation.getStartTime(),
                    updateOperation.getEndTime());
                break;
            }
            default:
                break;
        }
        return operationsToInject;
    }


    private void injectAccountSimples(List<Operation> operationsToInject, long accountId, double threshold,
                                      Date startTime, Date endTime) {
        injectSimple1(operationsToInject, accountId);
        injectSimple2(operationsToInject, accountId, startTime, endTime);
        injectSimple3(operationsToInject, accountId, threshold, startTime, endTime);
        injectSimple4(operationsToInject, accountId, threshold, startTime, endTime);
        injectSimple5(operationsToInject, accountId, threshold, startTime, endTime);
        injectSimple6(operationsToInject, accountId, startTime, endTime);
    }

    private void injectSimple1(List<Operation> operationsToInject, long accountId) {
        if (enabledShortReadOperationTypes.contains(SimpleRead1.class)) {
            operationsToInject.add(
                new SimpleRead1(accountId)
            );
        }
    }

    private void injectSimple2(List<Operation> operationsToInject, long accountId, Date startTime, Date endTime) {
        if (enabledShortReadOperationTypes.contains(SimpleRead2.class)) {
            operationsToInject.add(
                new SimpleRead2(accountId, startTime, endTime)
            );
        }
    }

    private void injectSimple3(List<Operation> operationsToInject, long accountId, double threshold,
                               Date startTime, Date endTime) {
        if (enabledShortReadOperationTypes.contains(SimpleRead3.class)) {
            operationsToInject.add(
                new SimpleRead3(accountId, threshold, startTime, endTime)
            );
        }
    }

    private void injectSimple4(List<Operation> operationsToInject, long accountId, double threshold,
                               Date startTime, Date endTime) {
        if (enabledShortReadOperationTypes.contains(SimpleRead4.class)) {
            operationsToInject.add(
                new SimpleRead4(accountId, threshold, startTime, endTime)
            );
        }
    }

    private void injectSimple5(List<Operation> operationsToInject, long accountId, double threshold,
                               Date startTime, Date endTime) {
        if (enabledShortReadOperationTypes.contains(SimpleRead5.class)) {
            operationsToInject.add(
                new SimpleRead5(accountId, threshold, startTime, endTime)
            );
        }
    }

    private void injectSimple6(List<Operation> operationsToInject, long accountId, Date startTime, Date endTime) {
        if (enabledShortReadOperationTypes.contains(SimpleRead6.class)) {
            operationsToInject.add(
                new SimpleRead6(accountId, startTime, endTime)
            );
        }
    }
}
