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
