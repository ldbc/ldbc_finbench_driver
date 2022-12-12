package org.ldbcouncil.finbench.driver.util;

public class ClassLoadingException extends Exception {

    public ClassLoadingException(String message) {
        super(message);
    }

    public ClassLoadingException() {
        super();
    }

    public ClassLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassLoadingException(Throwable cause) {
        super(cause);
    }
}
