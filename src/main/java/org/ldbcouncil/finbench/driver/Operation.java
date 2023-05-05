package org.ldbcouncil.finbench.driver;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.IOException;
import java.util.Map;
import org.ldbcouncil.finbench.driver.temporal.TemporalUtil;
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

/**
 * Describes Operation performed by the driver, e.g. queries.
 * Each Operation is expected to have a RESULT_TYPE, which is the expected
 * result of the opertion. The operation must be able to deserialize the
 * result object.
 */
@JsonTypeInfo(use = DEDUCTION)
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
})
public abstract class Operation<RESULT_TYPE> {
    private static final TemporalUtil temporalutil = new TemporalUtil();
    private long scheduledStartTimeAsMilli = -1;
    private long timeStamp = -1;
    private long dependencyTimeStamp = -1;
    private long expiryTimeStamp = -1;

    public final void setScheduledStartTimeAsMilli(long scheduledStartTimeAsMilli) {
        this.scheduledStartTimeAsMilli = scheduledStartTimeAsMilli;
    }

    public final void setDependencyTimeStamp(long dependencyTimeStamp) {
        this.dependencyTimeStamp = dependencyTimeStamp;
    }

    public final void setExpiryTimeStamp(long expiryTimeStamp) {
        this.expiryTimeStamp = expiryTimeStamp;
    }

    public final long scheduledStartTimeAsMilli() {
        return scheduledStartTimeAsMilli;
    }

    public final long dependencyTimeStamp() {
        return dependencyTimeStamp;
    }

    public long expiryTimeStamp() {
        return expiryTimeStamp;
    }

    public long timeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Get type of operation
     *
     * @return Type as integer
     */
    public abstract int type();

    @Override
    public String toString() {
        return "Operation{" + "scheduledStartTime=" + temporalutil.milliTimeToDateTimeString(scheduledStartTimeAsMilli)
            + ", timeStamp=" + temporalutil.milliTimeToDateTimeString(timeStamp) + ", dependencyTimeStamp="
            + temporalutil.milliTimeToDateTimeString(dependencyTimeStamp) + '}';
    }

    /**
     * Maps the operation variable of an operation to the getter function
     *
     * @return Map with operation variable as key and name of getter function
     */
    public abstract Map<String, Object> parameterMap();

    /**
     * Deserializes a list of result objects
     *
     * @param serializedOperationResult The serialized result object in a list
     * @return Deserialized result object
     * @throws IOException in case the given string cannot be deserialized to the result type
     */
    public abstract RESULT_TYPE deserializeResult(String serializedOperationResult) throws IOException;

    public Operation newInstance() {
        throw new UnsupportedOperationException();
    }
}
