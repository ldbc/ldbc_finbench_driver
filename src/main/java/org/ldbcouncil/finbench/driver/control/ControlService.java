package org.ldbcouncil.finbench.driver.control;

import org.ldbcouncil.finbench.driver.log.LoggingServiceFactory;
import org.ldbcouncil.finbench.driver.temporal.TimeSource;

public interface ControlService {
    DriverConfiguration configuration();

    LoggingServiceFactory loggingServiceFactory();

    TimeSource timeSource();

    void setWorkloadStartTimeAsMilli(long workloadStartTimeAsMilli);

    long workloadStartTimeAsMilli();

    void shutdown();
}
