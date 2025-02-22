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

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Factory for common {@link CharSeeker} implementations.
 */
public class CharSeekers {
    /**
     * Instantiates a {@link BufferedCharSeeker} with optional {@link ThreadAheadReadable read-ahead} capability.
     *
     * @param reader             the {@link CharReadable} which is the source of data, f.ex. a {@link FileReader}.
     * @param bufferSize         buffer size of the seeker and, if enabled, the read-ahead thread.
     * @param readAhead          whether or not to start a {@link ThreadAheadReadable read-ahead thread}
     *                           which strives towards always keeping one buffer worth of data read and available
     *                           from I/O when it's
     *                           time for the {@link BufferedCharSeeker} to read more data.
     * @param quotationCharacter character to interpret quotation character.
     * @return a {@link CharSeeker} with optional {@link ThreadAheadReadable read-ahead} capability.
     */
    public static CharSeeker charSeeker(CharReadable reader, int bufferSize, boolean readAhead,
                                        char quotationCharacter) {
        if (readAhead) {   // Thread that always has one buffer read ahead
            reader = ThreadAheadReadable.threadAhead(reader, bufferSize);
        }

        // Give the reader to the char seeker
        return new BufferedCharSeeker(reader, bufferSize, quotationCharacter);
    }

    /**
     * Instantiates a default {@link CharSeeker} capable of reading data in the specified {@code file}.
     *
     * @return {@link CharSeeker} reading and parsing data from {@code file}.
     * @throws FileNotFoundException if the specified {@code file} doesn't exist.
     */
    public static CharSeeker charSeeker(CharReadable reader, char quotationCharacter) throws FileNotFoundException {
        return charSeeker(reader, BufferedCharSeeker.DEFAULT_BUFFER_SIZE, true, quotationCharacter);
    }
}
