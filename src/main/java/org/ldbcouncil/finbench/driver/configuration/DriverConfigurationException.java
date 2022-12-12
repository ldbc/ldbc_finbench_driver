package org.ldbcouncil.finbench.driver.configuration;

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
