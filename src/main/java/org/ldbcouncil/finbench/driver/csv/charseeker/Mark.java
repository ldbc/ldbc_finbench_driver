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

import static java.lang.String.format;

/**
 * A mutable marker that is changed to hold progress made to a {@link BufferedCharSeeker}.
 * It holds information such as start/end position in the data stream, which character
 * was the match and whether or not this denotes the last value of the line.
 */
public class Mark {
    public static int END_OF_LINE_CHARACTER = -1;

    private int lineNumber;
    private long startPosition;
    private long position;
    private int character;

    /**
     * @param startPosition
     * @param position
     * @param character     use {@code -1} to denote that the matching character was an end-of-line or end-of-file
     */
    void set(int lineNumber, long startPosition, long position, int character) {
        this.lineNumber = lineNumber;
        this.startPosition = startPosition;
        this.position = position;
        this.character = character;
    }

    public int character() {
        assert !isEndOfLine();
        return character;
    }

    public boolean isEndOfLine() {
        return character == -1;
    }

    public int lineNumber() {
        return lineNumber;
    }

    long position() {
        if (position == -1) {
            throw new IllegalStateException("No value to extract here");
        }
        return position;
    }

    long startPosition() {
        if (startPosition == -1) {
            throw new IllegalStateException("No value to extract here");
        }
        return startPosition;
    }

    @Override
    public String toString() {
        return format("Mark[line:%d, from:%d, to:%d]", lineNumber, startPosition, position);
    }
}
