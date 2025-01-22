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

import org.ldbcouncil.finbench.driver.control.ControlService;
import org.ldbcouncil.finbench.driver.log.LoggingService;

public class PrintHelpMode implements DriverMode<Object> {
    private final ControlService controlService;
    private final LoggingService loggingService;

    public PrintHelpMode(ControlService controlService) {
        this.controlService = controlService;
        this.loggingService = controlService.loggingServiceFactory().loggingServiceFor(getClass().getSimpleName());
    }

    @Override
    public void init() throws DriverException {
    }

    @Override
    public Object startExecutionAndAwaitCompletion() throws DriverException {
        loggingService.info(controlService.configuration().helpString());
        return null;
    }

}
