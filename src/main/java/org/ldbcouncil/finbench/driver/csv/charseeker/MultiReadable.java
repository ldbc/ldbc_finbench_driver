/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ldbcouncil.finbench.driver.csv.charseeker;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

/**
 * Have multiple {@link CharReadable} instances look like one. The provided {@link CharReadable readables} should
 * be opened lazily, in {@link Iterator#next()}, and will be closed in here, if they implement {@link Closeable}.
 */
public class MultiReadable implements CharReadable, Closeable {
    private final RawIterator<CharReadable, IOException> actual;
    private CharReadable current = Readables.EMPTY;
    private int readFromCurrent;

    public MultiReadable(RawIterator<CharReadable, IOException> actual) {
        this.actual = actual;
    }

    @Override
    public int read(char[] buffer, int offset, int length) throws IOException {
        int read = 0;
        while (read < length) {
            int readThisTime = current.read(buffer, offset + read, length - read);
            if (readThisTime == -1) {
                if (actual.hasNext()) {
                    closeCurrent();
                    current = actual.next();

                    // Even if there's no line-ending at the end of this source we should introduce one
                    // otherwise the last line of this source and the first line of the next source will
                    // look like one long line.
                    if (readFromCurrent > 0) {
                        buffer[offset + read++] = '\n';
                        readFromCurrent = 0;
                    }
                } else {
                    break;
                }
            } else {
                read += readThisTime;
                readFromCurrent += readThisTime;
            }
        }
        return read == 0 ? -1 : read;
    }

    private void closeCurrent() throws IOException {
        current.close();
    }

    @Override
    public void close() throws IOException {
        closeCurrent();
    }
}
