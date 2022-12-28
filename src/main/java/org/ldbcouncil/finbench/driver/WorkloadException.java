package org.ldbcouncil.finbench.driver;

public class WorkloadException extends Exception {

    public WorkloadException(String message) {
        super(message);
    }

    public WorkloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
