package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload simple read query 6:
 * -- Companies with the same investor of exact company --
 * Given a Company, find all the Companies that the Persons invest.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.Operation;

public class SimpleRead6 extends Operation<List<SimpleRead6Result>> {
    public static final int TYPE = 106;
    public static final String ID = "id";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id;

    public SimpleRead6(@JsonProperty(ID) long id) {
        this.id = id;
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
    public List<SimpleRead6Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, SimpleRead6Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead6 that = (SimpleRead6) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SimpleRead6{"
            + "id="
            + id
            + '}';
    }
}

