package org.ldbcouncil.finbench.driver.workloads.dummy;


import java.util.Iterator;
import org.ldbcouncil.finbench.driver.Operation;

public class NothingOperationFactory implements Iterator<Operation> {
    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public NothingOperation next() {
        return new NothingOperation();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
