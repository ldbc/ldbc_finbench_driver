package org.ldbcouncil.finbench.driver.csv.charseeker;

import java.util.Iterator;

/**
 * Just like {@link Iterator}, but with the addition that {@link #hasNext()} and {@link #next()} can
 * be declared to throw a checked exception.
 *
 * @param <T>         type of items in this iterator.
 * @param <EXCEPTION> type of exception thrown from {@link #hasNext()} and {@link #next()}.
 */
public interface RawIterator<T, EXCEPTION extends Exception> {
    boolean hasNext() throws EXCEPTION;

    T next() throws EXCEPTION;

    void remove();
}
