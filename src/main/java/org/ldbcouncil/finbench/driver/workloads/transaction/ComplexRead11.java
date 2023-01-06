package org.ldbcouncil.finbench.driver.workloads.transaction;
/*
 * Transaction workload complex read query 11:
 * -- Final share analysis in investor relationship --
 * Given a Company, find all the Companies and Persons the Company invests by at most k steps. Then
calculate the final share ratio of the investors to the given company groupby the investor in de-
scending order.
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

public class ComplexRead11 extends Operation<List<ComplexRead11Result>> {
    public static final int TYPE = 11;
    public static final String ID = "id";
    public static final String K = "k";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final long id;
    private final int k;

    public ComplexRead11(@JsonProperty(ID) long id,
                         @JsonProperty(K) int k) {
        this.id = id;
        this.k = k;
    }

    public long getId() {
        return id;
    }

    public int getK() {
        return k;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(ID, id)
            .put(K, k)
            .build();
    }

    @Override
    public List<ComplexRead11Result> deserializeResult(String serializedOperationResult) throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(serializedOperationResult, ComplexRead11Result[].class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead11 that = (ComplexRead11) o;
        return id == that.id
            && k == that.k;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, k);
    }

    @Override
    public String toString() {
        return "ComplexRead11{"
            + "id="
            + id
            + ", k="
            + k
            + '}';
    }
}

