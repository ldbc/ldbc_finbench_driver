package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead7Result {
    public static final String COMP = "comp";
    private final long comp;

    public SimpleRead7Result(@JsonProperty(COMP) long comp) {
        this.comp = comp;
    }

    public long getComp() {
        return comp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead7Result that = (SimpleRead7Result) o;
        return comp == that.comp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(comp);
    }

    @Override
    public String toString() {
        return "SimpleRead7Result{"
            + "comp="
            + comp
            + '}';
    }
}

