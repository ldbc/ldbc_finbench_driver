package ldbc.finbench.driver.driver;

import ldbc.finbench.driver.configuration.AllDriverConfiguration;
import ldbc.finbench.driver.configuration.DriverConfigurationException;
import ldbc.finbench.driver.log.Log4jLoggingServiceFactory;
import ldbc.finbench.driver.log.LoggingService;
import ldbc.finbench.driver.log.LoggingServiceFactory;

public class Driver {
    public static void main(String[] args) {
        // Initial loggin service
        LoggingServiceFactory loggingServiceFactory = new Log4jLoggingServiceFactory( false );
        LoggingService loggingService = loggingServiceFactory.loggingServiceFor( Driver.class.getSimpleName() );

        //
        try {
            AllDriverConfiguration configuration = AllDriverConfiguration.fromArgs( args );
        } catch (DriverConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
