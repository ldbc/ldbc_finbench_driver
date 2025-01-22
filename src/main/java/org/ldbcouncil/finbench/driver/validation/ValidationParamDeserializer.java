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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class ValidationParamDeserializer extends StdDeserializer<ValidationParam> {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public ValidationParamDeserializer() {
        this(null);
    }

    public ValidationParamDeserializer(Class vc) {
        super(vc);
    }

    @Override
    public ValidationParam deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode operationNode = node.path("operation");
        JsonNode resultNode = node.path("operationResult");
        // TODO: Remove dependency on LdbcOperation valueType. This should be passed or set in a generic way
        Operation operation = OBJECT_MAPPER.readValue(operationNode.toPrettyString(), LdbcOperation.class);
        Object operationResult = operation.deserializeResult(resultNode.toPrettyString());
        return ValidationParam.createUntyped(operation, operationResult);
    }
}
