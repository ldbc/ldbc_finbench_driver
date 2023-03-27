package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ComplexRead11Result {
    public static final String ID = "id";
    public static final String TYPE = "type";
    public static final String RATIO = "ratio";
    private final long id;
    private final String type;
    private final float ratio;

    public ComplexRead11Result(@JsonProperty(ID) long id,
                               @JsonProperty(TYPE) String type,
                               @JsonProperty(RATIO) float ratio) {
        this.id = id;
        this.type = type;
        this.ratio = ratio;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public float getRatio() {
        return ratio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexRead11Result that = (ComplexRead11Result) o;
        return id == that.id
            && Objects.equals(type, that.type)
            && ratio == that.ratio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, ratio);
    }

    @Override
    public String toString() {
        return "ComplexRead11Result{"
            + "id="
            + id
            + ", type="
            + type
            + ", ratio="
            + ratio
            + '}';
    }
}

