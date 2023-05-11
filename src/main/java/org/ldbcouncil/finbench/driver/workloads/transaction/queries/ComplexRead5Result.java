package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.result.Path;

public class ComplexRead5Result {
    public static final String PATH = "path";
    private final Path path;

    public ComplexRead5Result(@JsonProperty(PATH) Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
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
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "ComplexRead5Result{"
            + "path="
            + path
            + '}';
    }
}

