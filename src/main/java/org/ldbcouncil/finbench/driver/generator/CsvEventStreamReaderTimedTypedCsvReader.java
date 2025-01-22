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

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.ldbcouncil.finbench.driver.util.Function1;


public class CsvEventStreamReaderTimedTypedCsvReader<BASE_EVENT_TYPE, DECODER_KEY_TYPE>
    implements Iterator<BASE_EVENT_TYPE> {
    private final Map<DECODER_KEY_TYPE, EventDecoder<BASE_EVENT_TYPE>> decoders;
    private final Iterator<String[]> csvRowIterator;
    private final Function1<String[], DECODER_KEY_TYPE, RuntimeException> decoderKeyExtractor;

    public CsvEventStreamReaderTimedTypedCsvReader(Iterator<String[]> csvRowIterator,
                                                   Map<DECODER_KEY_TYPE, EventDecoder<BASE_EVENT_TYPE>> decoders,
                                                   Function1<String[], DECODER_KEY_TYPE,
                                                       RuntimeException> decoderKeyExtractor) {
        this.csvRowIterator = csvRowIterator;
        this.decoders = decoders;
        this.decoderKeyExtractor = decoderKeyExtractor;
    }

    @Override
    public boolean hasNext() {
        return csvRowIterator.hasNext();
    }

    @Override
    public BASE_EVENT_TYPE next() {
        String[] csvRow = csvRowIterator.next();
        DECODER_KEY_TYPE decoderKey = decoderKeyExtractor.apply(csvRow);
        EventDecoder<BASE_EVENT_TYPE> decoder = decoders.get(decoderKey);
        if (null == decoder) {
            throw new NoSuchElementException(
                format("No decoder found that matches this column\nROW: %s\nDECODER KEY: %s", Arrays.toString(csvRow),
                    decoderKey));
        }
        return decoder.decodeEvent(csvRow);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException(format("%s does not support remove()", getClass().getSimpleName()));
    }


    public interface EventDecoder<BASE_EVENT_TYPE> {
        BASE_EVENT_TYPE decodeEvent(String[] csvRow);
    }
}
