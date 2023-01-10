package org.ldbcouncil.finbench.driver.driver;

public interface DriverMode<T> {
    void init() throws DriverException;

    T startExecutionAndAwaitCompletion() throws DriverException;
}
