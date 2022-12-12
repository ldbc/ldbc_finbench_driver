package org.ldbcouncil.finbench.driver.generator;

public class GeneratorException extends RuntimeException {


    public GeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneratorException(String message) {
        super(message);
    }
}
