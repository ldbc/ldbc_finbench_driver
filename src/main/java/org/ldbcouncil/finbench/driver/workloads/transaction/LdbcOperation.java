package org.ldbcouncil.finbench.driver.workloads.transaction;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead2;

/**
 * LdbcOperation.java
 * Wrapper abstract class to define the types of Operation classes the
 * LDBC Finbench workload implements.
 */

@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
@JsonSubTypes({
    // TODO add every Query
    @JsonSubTypes.Type(ComplexRead1.class),
    @JsonSubTypes.Type(ComplexRead2.class)
})
public abstract class LdbcOperation<LDBC_RESULT_TYPE> extends Operation<LDBC_RESULT_TYPE> {

}
