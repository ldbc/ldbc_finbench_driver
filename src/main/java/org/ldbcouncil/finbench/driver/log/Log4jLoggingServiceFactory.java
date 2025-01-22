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
