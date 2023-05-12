package org.ldbcouncil.finbench.driver.result;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathDeserializer extends JsonDeserializer<Path> {

    @Override
    public Path deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        List<Long> path = new ArrayList<>();
        while (p.nextToken() != null && p.currentToken() != JsonToken.END_ARRAY) {
            path.add(p.getLongValue());
        }
        return new Path(path);
    }
}
