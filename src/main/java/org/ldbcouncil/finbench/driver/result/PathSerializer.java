package org.ldbcouncil.finbench.driver.result;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class PathSerializer extends JsonSerializer<Path> {

    @Override
    public void serialize(
        Path value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
        gen.writeStartArray(value.getPath().size());
        for (long id : value.getPath()) {
            gen.writeNumber(id);
        }
        gen.writeEndArray();
    }
}
