package org.ldbcouncil.finbench.driver.result;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// The Type of path, [ID1, ID2, ID3, ID4...]
@JsonSerialize(using = PathSerializer.class)
@JsonDeserialize(using = PathDeserializer.class)
public class Path {
    private List<Long> path;

    public Path(List<Long> path) {
        this.path = path;
    }

    public Path() {
        path = new ArrayList<>();
    }

    public void setPath(List<Long> path) {
        this.path = path;
    }

    public void addId(Long id) {
        this.path.add(id);
    }

    public List<Long> getPath() {
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
        Path path1 = (Path) o;
        return Objects.equals(path, path1.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "Path{"
            + "path=" + path
            + '}';
    }
}
