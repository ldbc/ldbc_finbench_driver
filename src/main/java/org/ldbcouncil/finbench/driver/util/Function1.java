package org.ldbcouncil.finbench.driver.util;

public interface Function1<INPUT, RETURN, EXCEPTION extends Exception> {
    RETURN apply(INPUT input) throws EXCEPTION;
}
