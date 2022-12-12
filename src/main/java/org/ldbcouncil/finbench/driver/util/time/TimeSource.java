package org.ldbcouncil.finbench.driver.util.time;

public interface TimeSource {
    // Avoid object creation where possible
    // Only use for measuring of short durations
    long nanoSnapshot();

    // Avoid object creation where possible
    long nowAsMilli();
}
