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

package org.ldbcouncil.finbench.driver.validation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.ldbcouncil.finbench.driver.Operation;

public class ValidationParamSerializer extends StdSerializer<Object> {

    public ValidationParamSerializer() {
        this(null);
    }

    public ValidationParamSerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object result, JsonGenerator generator, SerializerProvider provider) throws IOException {
        ValidationParam param = (ValidationParam) result;
        Operation operation = param.operation();
        Object operationResult = param.operationResult();
        generator.writeStartObject();
        generator.writeObjectField("operation", operation);
        generator.writeObjectField("operationResult", operationResult);
        generator.writeEndObject();
    }
}
