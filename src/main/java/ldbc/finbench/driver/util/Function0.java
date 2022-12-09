package ldbc.finbench.driver.util;

public interface Function0<RETURN, EXCEPTION extends Exception> {
    RETURN apply() throws EXCEPTION;
}
