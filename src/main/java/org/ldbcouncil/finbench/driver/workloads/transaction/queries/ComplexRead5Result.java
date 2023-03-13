package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.result.Path;

public class ComplexRead5Result {
    public static final String PATHS = "paths";
    private final Path paths;

    public ComplexRead5Result(@JsonProperty(PATHS) Path paths) {
        this.paths = paths;
    }

    public Path getPaths() {
        return paths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead5Result that = (ComplexRead5Result) o;
        return paths == that.paths;
    }

    @Override
    public int hashCode() {
        return Objects.hash(paths);
    }

    @Override
    public String toString() {
        return "ComplexRead5Result{"
            + "paths="
            + paths
            + '}';
    }
}

