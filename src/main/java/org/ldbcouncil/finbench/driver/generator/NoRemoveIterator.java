package org.ldbcouncil.finbench.driver.generator;

import static java.lang.String.format;

import java.util.Iterator;

public abstract class NoRemoveIterator<TYPE> implements Iterator<TYPE> {
    @Override
    public void remove() {
        throw new UnsupportedOperationException(format("%s does not support remove()", getClass().getSimpleName()));
    }
}
