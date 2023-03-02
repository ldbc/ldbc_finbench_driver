package org.ldbcouncil.finbench.driver.runtime.metrics;

import java.io.IOException;

public class NullResultsLogWriter implements ResultsLogWriter {
    @Override
    public void write(
        String operationName,
        long scheduledStartTimeAsMilli,
        long actualStartTimeAsMilli,
        long runDurationAsNano,
        int resultCode,
        long originalStartTime) throws IOException {
        // do nothing
    }

    @Override
    public void close() throws Exception {
        // do nothing
    }
}
