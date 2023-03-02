package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload simple read query 3:
 * -- Company-related information --
 * Given a Company, find all the related nodes.
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

public class SimpleRead3 extends Operation<List<SimpleRead3Result>> {
    public static final int TYPE = 103;
    public static final String ID = "id";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id;

    public SimpleRead3(@JsonProperty(ID) long id) {
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
    public List<SimpleRead3Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, SimpleRead3Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead3 that = (SimpleRead3) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SimpleRead3{"
            + "id="
            + id
            + '}';
    }
}

