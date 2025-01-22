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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.ldbcouncil.finbench.driver.Workload;

public class ValidationParamsFromJson {

    private final File jsonFile;
    private final ObjectMapper OBJECT_MAPPER;

    public ValidationParamsFromJson(File jsonFile, Workload workload) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerSubtypes(workload.getOperationClass());
        OBJECT_MAPPER = mapper;
        this.jsonFile = jsonFile;
    }

    public List<ValidationParam> deserialize() throws IOException {
        return Arrays.asList(OBJECT_MAPPER.readValue(jsonFile, ValidationParam[].class));
    }
}
