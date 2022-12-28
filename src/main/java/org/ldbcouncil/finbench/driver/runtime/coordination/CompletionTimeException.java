package org.ldbcouncil.finbench.driver.runtime.coordination;

public class CompletionTimeException extends Exception {

    public CompletionTimeException(String message) {
        super(message);
    }

    public CompletionTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
