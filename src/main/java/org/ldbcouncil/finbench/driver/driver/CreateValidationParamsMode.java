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

package org.ldbcouncil.finbench.driver.driver;

import static java.lang.String.format;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.ldbcouncil.finbench.driver.Db;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.WorkloadStreams;
import org.ldbcouncil.finbench.driver.control.ControlService;
import org.ldbcouncil.finbench.driver.generator.GeneratorFactory;
import org.ldbcouncil.finbench.driver.generator.RandomDataGeneratorFactory;
import org.ldbcouncil.finbench.driver.log.LoggingService;
import org.ldbcouncil.finbench.driver.util.ClassLoaderHelper;
import org.ldbcouncil.finbench.driver.util.Tuple3;
import org.ldbcouncil.finbench.driver.validation.ValidationParam;
import org.ldbcouncil.finbench.driver.validation.ValidationParamsGenerator;
import org.ldbcouncil.finbench.driver.validation.ValidationParamsToJson;

public class CreateValidationParamsMode implements DriverMode<Object> {
    private final ControlService controlService;
    private final LoggingService loggingService;
    private final long randomSeed;

    private Workload workload = null;
    private Db database = null;
    private Iterator<Operation> timeMappedOperations = null;

    /**
     * Create class to generate validation queries.
     *
     * @param controlService Object with functions to time, log and stored init configuration
     * @param randomSeed     The random seed used for the data generator
     * @throws DriverException
     */
    public CreateValidationParamsMode(ControlService controlService, long randomSeed) throws DriverException {
        this.controlService = controlService;
        this.loggingService = controlService.loggingServiceFactory().loggingServiceFor(getClass().getSimpleName());
        this.randomSeed = randomSeed;
    }

    /**
     * Initializes the validation parameter class. This loads the configuration given through
     * validation.properties and the database to use to generate the validation parameters.
     */
    @Override
    public void init() throws DriverException {
        try {
            workload = ClassLoaderHelper.loadWorkload(controlService.configuration().workloadClassName());
            workload.init(controlService.configuration());
        } catch (Exception e) {
            throw new DriverException(format("Error loading Workload class: %s",
                controlService.configuration().workloadClassName()), e);
        }
        loggingService.info(format("Loaded Workload: %s", workload.getClass().getName()));

        try {
            database = ClassLoaderHelper.loadDb(controlService.configuration().dbClassName());
            database.init(
                controlService.configuration().asMap(),
                controlService.loggingServiceFactory().loggingServiceFor(database.getClass().getSimpleName()),
                workload.operationTypeToClassMapping()
            );
        } catch (Exception e) {
            throw new DriverException(
                format("Error loading DB class: %s", controlService.configuration().dbClassName()), e);
        }
        loggingService.info(format("Loaded DB: %s", database.getClass().getName()));

        GeneratorFactory gf = new GeneratorFactory(new RandomDataGeneratorFactory(randomSeed));

        loggingService.info(
            format("Retrieving operation stream for workload: %s", workload.getClass().getSimpleName()));
        try {
            boolean returnStreamsWithDbConnector = false;
            Tuple3<WorkloadStreams, Workload, Long> streamsAndWorkload =
                WorkloadStreams.createNewWorkloadWithOffsetAndLimitedWorkloadStreams(
                    controlService.configuration(),
                    gf,
                    returnStreamsWithDbConnector,
                    0,
                    controlService.configuration().operationCount(),
                    controlService.loggingServiceFactory()
                );
            workload = streamsAndWorkload._2();
            WorkloadStreams workloadStreams = streamsAndWorkload._1();
            timeMappedOperations =
                WorkloadStreams.mergeSortedByStartTimeExcludingChildOperationGenerators(gf, workloadStreams);
        } catch (Exception e) {
            throw new DriverException("Error while retrieving operation stream for workload", e);
        }

        loggingService.info("Driver Configuration");
        loggingService.info(controlService.toString());
    }

    /**
     * Create validation parameters.
     */
    @Override
    public Object startExecutionAndAwaitCompletion() throws DriverException {
        try (Workload w = workload; Db db = database) {
            File validationFileToGenerate =
                new File(controlService.configuration().databaseValidationFilePath());

            int validationSetSize = controlService.configuration().validationParametersSize();

            loggingService.info(
                format("Generating database validation file: %s", validationFileToGenerate.getAbsolutePath()));

            Iterator<ValidationParam> validationParamsGenerator = new ValidationParamsGenerator(
                db,
                w.dbValidationParametersFilter(validationSetSize),
                timeMappedOperations,
                validationSetSize
            );
            List<ValidationParam> validationParams = ImmutableList.copyOf(validationParamsGenerator);
            ValidationParamsToJson validationParamsAsJson = new ValidationParamsToJson(
                validationParams,
                workload,
                controlService.configuration().validationSerializationCheck()
            );
            validationParamsAsJson.serializeValidationParameters(validationFileToGenerate);

            loggingService.info(format("Successfully generated %s database validation parameters",
                validationParams.size()));
        } catch (Exception e) {
            throw new DriverException("Error encountered duration validation parameter creation", e);
        }
        return null;
    }
}
