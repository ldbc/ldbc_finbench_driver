package ldbc.finbench.driver.log;

public interface LoggingServiceFactory
{
    LoggingService loggingServiceFor( String source );
}
