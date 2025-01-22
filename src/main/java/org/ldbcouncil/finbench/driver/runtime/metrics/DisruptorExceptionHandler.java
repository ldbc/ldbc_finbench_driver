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

package org.ldbcouncil.finbench.driver.runtime.metrics;

import static java.lang.String.format;

import com.lmax.disruptor.ExceptionHandler;
import org.ldbcouncil.finbench.driver.runtime.ConcurrentErrorReporter;

public class DisruptorExceptionHandler implements ExceptionHandler {
    private final ConcurrentErrorReporter errorReporter;

    public DisruptorExceptionHandler(ConcurrentErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
    }

    @Override
    public void handleEventException(Throwable throwable, long l, Object o) {
        errorReporter.reportError(
            this,
            format("Disruptor encountered error on event\nl = %s\no = %s\n%s",
                l,
                o == null ? "null" : o.toString(),
                ConcurrentErrorReporter.stackTraceToString(throwable)
            )
        );
    }

    @Override
    public void handleOnStartException(Throwable throwable) {
        errorReporter.reportError(
            this,
            format("Disruptor encountered error on start\n%s",
                ConcurrentErrorReporter.stackTraceToString(throwable)
            )
        );
    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {
        errorReporter.reportError(
            this,
            format("%s encountered error on shutdown\n%s",
                DisruptorSbeMetricsService.class.getSimpleName(),
                ConcurrentErrorReporter.stackTraceToString(throwable)
            )
        );
    }
}
