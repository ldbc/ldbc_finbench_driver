package org.ldbcouncil.finbench.driver.csv.charseeker;

/**
 * Generic function interface to map from one type to another.
 * <p/>
 * This can be used with the Iterables methods to transform lists of objects.
 */
public interface RawFunction<FROM, TO, EXCEPTION extends Exception> {
    /**
     * Apply a value to this function
     *
     * @param from the input item
     * @return the mapped item
     * @throws an exception if the function fails.
     */
    TO apply(FROM from) throws EXCEPTION;
}
