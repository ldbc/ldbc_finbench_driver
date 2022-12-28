package org.ldbcouncil.finbench.driver.temporal;

public class TemporalException extends RuntimeException {

    public TemporalException(String message) {
        super(message);
    }

    public TemporalException() {
        super();
    }

    public TemporalException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemporalException(Throwable cause) {
        super(cause);
    }
}
