package org.ldbcouncil.finbench.driver.driver;

import org.ldbcouncil.finbench.driver.control.ConsoleAndFileDriverConfiguration;
import org.ldbcouncil.finbench.driver.control.DriverConfigurationException;
import org.ldbcouncil.finbench.driver.log.Log4jLoggingServiceFactory;
import org.ldbcouncil.finbench.driver.log.LoggingService;
import org.ldbcouncil.finbench.driver.log.LoggingServiceFactory;

public class Driver {
    public static void main(String[] args) {
        // Initial loggin service
        LoggingServiceFactory loggingServiceFactory = new Log4jLoggingServiceFactory(false);
        LoggingService loggingService = loggingServiceFactory.loggingServiceFor(Driver.class.getSimpleName());

        //
        try {
            ConsoleAndFileDriverConfiguration configuration = ConsoleAndFileDriverConfiguration.fromArgs(args);
        } catch (DriverConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
