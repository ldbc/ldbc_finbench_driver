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
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * A {@link Readable}, but focused on {@code char[]} instead of {@link CharBuffer}, with the main reaon
 * that {@link Reader#read(CharBuffer)} creates a new {@code char[]} as big as the data it's about to read
 * every call. However {@link Reader#read(char[], int, int)} doesn't, and so leaves no garbage.
 * <p/>
 * The fact that this is a separate interface means that {@link Readable} instances need to be wrapped,
 * but that's fine since the buffer size should be reasonably big such that {@link #read(char[], int, int)}
 * isn't called too often. Therefore the wrapping overhead should not be noticeable at all.
 * <p/>
 * Also took the opportunity to let {@link CharReadable} extends {@link Closeable}, something that
 * {@link Readable} doesn't.
 */
public interface CharReadable extends Closeable {
    /**
     * Reads characters into a portion of an array. This method will block until some input is available,
     * an I/O error occurs, or the end of the stream is reached.
     *
     * @param buffer {@code char[]} buffer to read the data into.
     * @param offset offset at which to start storing characters in {@code buffer}.
     * @param length maximum number of characters to read.
     * @return the number of characters read, or -1 if the end of the stream has been reached.
     * @throws IOException if an I/O error occurs.
     */
    int read(char[] buffer, int offset, int length) throws IOException;
}
