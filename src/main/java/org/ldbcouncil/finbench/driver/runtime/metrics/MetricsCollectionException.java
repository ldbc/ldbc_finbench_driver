package org.ldbcouncil.finbench.driver.runtime.metrics;

public class MetricsCollectionException extends Exception {

    public MetricsCollectionException(String message) {
        super(message);
    }

    public MetricsCollectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
