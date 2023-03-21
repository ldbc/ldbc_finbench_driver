package org.ldbcouncil.finbench.impls.dummy;

import java.io.IOException;
import org.ldbcouncil.finbench.driver.DbConnectionState;

public class DummyDbConnectionState extends DbConnectionState {

    public DummyDbConnectionState() {
    }

    @Override
    public void close() throws IOException {
    }

}
