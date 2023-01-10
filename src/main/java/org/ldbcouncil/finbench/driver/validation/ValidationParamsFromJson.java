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
