package org.ldbcouncil.finbench.driver.log;

import org.ldbcouncil.finbench.driver.temporal.TemporalUtil;

/**
 * The logging instance entry for FinBench.
 * TODO: simplify this. Seems no need to use a factory
 */
public class Log4jLoggingServiceFactory implements LoggingServiceFactory {
    private final TemporalUtil temporalUtil;
    private final boolean detailedStatus;

    public Log4jLoggingServiceFactory(boolean detailedStatus) {
        this.temporalUtil = new TemporalUtil();
        this.detailedStatus = detailedStatus;
    }

    public LoggingService loggingServiceFor(String source) {
        return new Log4jLoggingService(source, temporalUtil, detailedStatus);
    }
}
