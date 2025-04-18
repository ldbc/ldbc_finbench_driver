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

package org.ldbcouncil.finbench.driver.generator;

import static java.lang.String.format;

import java.io.IOException;
import java.util.Iterator;
import org.ldbcouncil.finbench.driver.csv.charseeker.CharSeeker;
import org.ldbcouncil.finbench.driver.csv.charseeker.Extractors;
import org.ldbcouncil.finbench.driver.csv.charseeker.Mark;


public class CsvEventStreamReaderBasicCharSeeker<BASE_EVENT_TYPE> implements Iterator<BASE_EVENT_TYPE> {
    private final EventDecoder<BASE_EVENT_TYPE> decoder;
    private final CharSeeker charSeeker;
    private final Extractors extractors;
    private final Mark mark;
    private final int[] columnDelimiters;
    private BASE_EVENT_TYPE nextEvent = null;

    public CsvEventStreamReaderBasicCharSeeker(
        CharSeeker charSeeker,
        Extractors extractors,
        Mark mark,
        EventDecoder<BASE_EVENT_TYPE> decoder,
        int columnDelimiter) {
        this.charSeeker = charSeeker;
        this.extractors = extractors;
        this.mark = mark;
        this.columnDelimiters = new int[] {columnDelimiter};
        this.decoder = decoder;
    }

    @Override
    public boolean hasNext() {
        if (null == nextEvent) {
            nextEvent = getNextEvent();
        }
        return null != nextEvent;
    }

    @Override
    public BASE_EVENT_TYPE next() {
        if (null == nextEvent) {
            nextEvent = getNextEvent();
        }
        BASE_EVENT_TYPE result = nextEvent;
        nextEvent = null;
        return result;
    }

    BASE_EVENT_TYPE getNextEvent() {
        try {
            return decoder.decodeEvent(charSeeker, extractors, columnDelimiters, mark);
        } catch (IOException e) {
            throw new GeneratorException("Error while retrieving next event", e);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException(
            format("%s does not support remove()", getClass().getSimpleName()));
    }


    public interface EventDecoder<BASE_EVENT_TYPE> {
        BASE_EVENT_TYPE decodeEvent(CharSeeker charSeeker, Extractors extractors, int[] columnDelimiters, Mark mark)
            throws IOException;
    }
}
