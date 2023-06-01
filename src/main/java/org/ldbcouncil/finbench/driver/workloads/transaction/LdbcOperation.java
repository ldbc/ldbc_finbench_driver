package org.ldbcouncil.finbench.driver.workloads.transaction;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead10;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead11;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead12;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead6;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead7;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead8;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead9;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead6;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write10;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write11;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write12;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write13;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write14;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write15;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write16;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write17;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write18;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write19;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write6;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write7;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write8;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write9;

/**
 * LdbcOperation.java
 * Wrapper abstract class to define the types of Operation classes the
 * LDBC Finbench workload implements.
 */

@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
@JsonSubTypes({
    @JsonSubTypes.Type(ComplexRead1.class),
    @JsonSubTypes.Type(ComplexRead2.class),
    @JsonSubTypes.Type(ComplexRead3.class),
    @JsonSubTypes.Type(ComplexRead4.class),
    @JsonSubTypes.Type(ComplexRead5.class),
    @JsonSubTypes.Type(ComplexRead6.class),
    @JsonSubTypes.Type(ComplexRead7.class),
    @JsonSubTypes.Type(ComplexRead8.class),
    @JsonSubTypes.Type(ComplexRead9.class),
    @JsonSubTypes.Type(ComplexRead10.class),
    @JsonSubTypes.Type(ComplexRead11.class),
    @JsonSubTypes.Type(ComplexRead12.class),
    @JsonSubTypes.Type(SimpleRead1.class),
    @JsonSubTypes.Type(SimpleRead2.class),
    @JsonSubTypes.Type(SimpleRead3.class),
    @JsonSubTypes.Type(SimpleRead4.class),
    @JsonSubTypes.Type(SimpleRead5.class),
    @JsonSubTypes.Type(SimpleRead6.class),
    @JsonSubTypes.Type(Write1.class),
    @JsonSubTypes.Type(Write2.class),
    @JsonSubTypes.Type(Write3.class),
    @JsonSubTypes.Type(Write4.class),
    @JsonSubTypes.Type(Write5.class),
    @JsonSubTypes.Type(Write6.class),
    @JsonSubTypes.Type(Write7.class),
    @JsonSubTypes.Type(Write8.class),
    @JsonSubTypes.Type(Write9.class),
    @JsonSubTypes.Type(Write10.class),
    @JsonSubTypes.Type(Write11.class),
    @JsonSubTypes.Type(Write12.class),
    @JsonSubTypes.Type(Write13.class),
    @JsonSubTypes.Type(Write14.class),
    @JsonSubTypes.Type(Write15.class),
    @JsonSubTypes.Type(Write16.class),
    @JsonSubTypes.Type(Write17.class),
    @JsonSubTypes.Type(Write18.class),
    @JsonSubTypes.Type(Write19.class),
    @JsonSubTypes.Type(ReadWrite1.class),
    @JsonSubTypes.Type(ReadWrite2.class),
    @JsonSubTypes.Type(ReadWrite3.class)

})
public abstract class LdbcOperation<LDBC_RESULT_TYPE> extends Operation<LDBC_RESULT_TYPE> {

}
