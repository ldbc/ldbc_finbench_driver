package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload simple read query 1:
 * -- Exact account query --
 * Given an id of an Account, find the properties of the specific Account.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class SimpleRead1 extends LdbcOperation<List<SimpleRead1Result>> {
    public static final int TYPE = 101;
    public static final String ID = "id";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id;

    public SimpleRead1(@JsonProperty(ID) long id) {
        this.id = id;
    }

    public SimpleRead1(SimpleRead1 operation) {
        this.id = operation.id;
    }

    @Override
    public SimpleRead1 newInstance() {
        return new SimpleRead1(this);
    }

    public long getId() {
        return id;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(ID, id)
            .build();
    }

    @Override
    public List<SimpleRead1Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, SimpleRead1Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead1 that = (SimpleRead1) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SimpleRead1{"
            + "id="
            + id
            + '}';
    }
}

