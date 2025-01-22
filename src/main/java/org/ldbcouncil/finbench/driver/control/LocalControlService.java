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

package org.ldbcouncil.finbench.driver.control;

import org.ldbcouncil.finbench.driver.log.LoggingServiceFactory;
import org.ldbcouncil.finbench.driver.temporal.TemporalUtil;
import org.ldbcouncil.finbench.driver.temporal.TimeSource;

public class LocalControlService implements ControlService {
    private final TemporalUtil temporalUtil = new TemporalUtil();
    private final DriverConfiguration configuration;
    private final LoggingServiceFactory loggingServiceFactory;
    private final TimeSource timeSource;
    private long workloadStartTimeAsMilli;

    public LocalControlService(
        long workloadStartTimeAsMilli,
        DriverConfiguration configuration,
        LoggingServiceFactory loggingServiceFactory,
        TimeSource timeSource) {
        this.workloadStartTimeAsMilli = workloadStartTimeAsMilli;
        this.configuration = configuration;
        this.loggingServiceFactory = loggingServiceFactory;
        this.timeSource = timeSource;
    }

    @Override
    public DriverConfiguration configuration() {
        return configuration;
    }

    @Override
    public LoggingServiceFactory loggingServiceFactory() {
        return loggingServiceFactory;
    }

    @Override
    public TimeSource timeSource() {
        return timeSource;
    }

    @Override
    public void setWorkloadStartTimeAsMilli(long workloadStartTimeAsMilli) {
        this.workloadStartTimeAsMilli = workloadStartTimeAsMilli;
    }

    @Override
    public long workloadStartTimeAsMilli() {
        return workloadStartTimeAsMilli;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public String toString() {
        return "Workload Start Time:\t"
            + temporalUtil.milliTimeToDateTimeString(workloadStartTimeAsMilli)
            + "\n"
            + configuration.toString();
    }
}
