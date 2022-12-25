package org.ldbcouncil.finbench.driver;

public class OperationException extends Exception {

    public OperationException(String message) {
        super(message);
    }

    public OperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
