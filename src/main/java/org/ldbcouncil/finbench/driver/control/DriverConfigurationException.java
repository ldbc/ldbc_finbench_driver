package org.ldbcouncil.finbench.driver.control;

public class DriverConfigurationException extends Exception {

    public DriverConfigurationException(String message) {
        super(message);
    }

    public DriverConfigurationException() {
        super();
    }

    public DriverConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DriverConfigurationException(Throwable cause) {
        super(cause);
    }
}
