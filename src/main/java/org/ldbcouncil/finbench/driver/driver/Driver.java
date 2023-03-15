package org.ldbcouncil.finbench.driver.driver;

import static java.lang.String.format;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.ldbcouncil.finbench.driver.control.ConsoleAndFileDriverConfiguration;
import org.ldbcouncil.finbench.driver.control.ControlService;
import org.ldbcouncil.finbench.driver.control.DriverConfigurationException;
import org.ldbcouncil.finbench.driver.control.LocalControlService;
import org.ldbcouncil.finbench.driver.control.OperationMode;
import org.ldbcouncil.finbench.driver.log.Log4jLoggingServiceFactory;
import org.ldbcouncil.finbench.driver.log.LoggingService;
import org.ldbcouncil.finbench.driver.log.LoggingServiceFactory;
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;
import org.ldbcouncil.finbench.driver.temporal.SystemTimeSource;
import org.ldbcouncil.finbench.driver.temporal.TimeSource;

public class Driver {
    private static final long RANDOM_SEED = 42;

    public static void main(String[] args) {
        ControlService controlService = null;
        boolean detailedStatus = false;
        // Initial loggin service
        LoggingServiceFactory loggingServiceFactory = new Log4jLoggingServiceFactory(detailedStatus);
        LoggingService loggingService = loggingServiceFactory.loggingServiceFor(Driver.class.getSimpleName());

        //
        try {
            TimeSource systemTimeSource = new SystemTimeSource();
            ConsoleAndFileDriverConfiguration configuration = ConsoleAndFileDriverConfiguration.fromArgs(args);
            long workloadStartTimeAsMilli = systemTimeSource.nowAsMilli() + TimeUnit.SECONDS.toMillis(5);
            controlService = new LocalControlService(
                workloadStartTimeAsMilli,
                configuration,
                loggingServiceFactory,
                systemTimeSource);
            Driver driver = new Driver();
            DriverMode<?> driverMode = driver.getDriverModeFor(controlService);
            driverMode.init();
            driverMode.startExecutionAndAwaitCompletion();

        } catch (DriverConfigurationException e) {
            String errMsg = format("Error parsing parameters: %s", e.getMessage());
            loggingService.info(errMsg);
            System.exit(1);
        } catch (Exception e) {
            loggingService.info("Driver terminated unexpectedly\n" + ConcurrentErrorReporter.stackTraceToString(e));
            System.exit(1);
        } finally {
            if (null != controlService) {
                controlService.shutdown();
            }
        }
    }

    /**
     * Create instance of operation mode.
     *
     * @param controlService ControlService with loaded DriverConfiguration
     * @return DriverMode object with specified operation mode
     * @throws DriverException When one or more required parameters are missing
     */
    public DriverMode<?> getDriverModeFor(ControlService controlService) throws DriverException {
        if (controlService.configuration().shouldPrintHelpString()) {
            return new PrintHelpMode(controlService);
        }

        List<String> missingParams =
            ConsoleAndFileDriverConfiguration.checkMissingParams(controlService.configuration());
        if (!missingParams.isEmpty()) {
            throw new DriverException(format("Missing required parameters: %s", missingParams.toString()));
        }

        OperationMode mode = OperationMode.valueOf(controlService.configuration().mode());

        switch (mode) {
            case CREATE_VALIDATION:
                return new CreateValidationParamsMode(controlService, RANDOM_SEED);
            case CREATE_STATISTICS:
                return new CalculateWorkloadStatisticsMode(controlService, RANDOM_SEED);
            case VALIDATE_DATABASE:
                return new ValidateDatabaseMode(controlService);
            case EXECUTE_BENCHMARK:
            default: // Execute benchmark is default behaviour
                return new ExecuteWorkloadMode(controlService, new SystemTimeSource(), RANDOM_SEED);
        }
    }
}
