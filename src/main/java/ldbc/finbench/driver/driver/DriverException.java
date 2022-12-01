package ldbc.finbench.driver.driver;

public class DriverException extends Exception {
    private static final long serialVersionUID = 7166804842129940500L;

    public DriverException(String message) {
        super(message);
    }

    public DriverException(String message, Throwable cause) {
        super(message, cause);
    }

}
