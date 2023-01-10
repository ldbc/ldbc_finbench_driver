package org.ldbcouncil.finbench.driver.generator;

import java.sql.ResultSet;
import org.ldbcouncil.finbench.driver.WorkloadException;

public class EventStreamReader<BASE_EVENT_TYPE> {
    public interface EventDecoder<BASE_EVENT_TYPE> {
        BASE_EVENT_TYPE decodeEvent(ResultSet rs)
            throws WorkloadException;
    }
}
