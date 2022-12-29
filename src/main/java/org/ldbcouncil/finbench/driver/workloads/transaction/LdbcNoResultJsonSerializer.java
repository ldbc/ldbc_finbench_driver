package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

/*
 * The LdbcNoResult is serialized to -1, which requires custom serialization.
 */
public class LdbcNoResultJsonSerializer extends StdSerializer<Object> {

    public LdbcNoResultJsonSerializer() {
        this(null);
    }

    public LdbcNoResultJsonSerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object result, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeString(
            Integer.toString(LdbcFinBenchTransactionWorkloadConfiguration.WRITE_OPERATION_NO_RESULT_DEFAULT_RESULT));
    }
}
