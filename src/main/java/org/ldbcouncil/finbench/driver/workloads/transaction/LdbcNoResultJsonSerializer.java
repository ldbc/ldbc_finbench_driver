/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
