package org.ldbcouncil.finbench.driver.csv.charseeker;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * Seeks for specific characters in a stream of characters, e.g. a {@link Reader}. Uses a {@link Mark}
 * as keeper of position. Once a {@link #seek(Mark, int[])} has succeeded the characters specified by
 * the mark can be {@link #extract(Mark, Extractor) extracted} into a value of an arbitrary type.
 * <p/>
 * Typical usage is:
 * <p/>
 * <pre>
 * CharSeeker seeker = ...
 * Mark mark = new Mark();
 * int[] delimiters = new int[] {'\t',','};
 *
 * while ( seeker.seek( mark, delimiters ) )
 * {
 *     String value = seeker.extract( mark, Extractors.STRING );
 *     // ... somehow manage the value
 *     if ( mark.isEndOfLine() )
 *     {
 *         // ... end of line, put some logic to handle that here
 *     }
 * }
 * </pre>
 * <p/>
 * The {@link Reader} that gets passed in will be closed in {@link #close()}.
 *
 * @author Mattias Persson
 */
public interface CharSeeker extends Closeable {
    CharSeeker EMPTY = new CharSeeker() {
        @Override
        public boolean seek(Mark mark, int[] untilOneOfChars) {
            return false;
        }

        @Override
        public <EXTRACTOR extends Extractor<?>> EXTRACTOR extract(Mark mark, EXTRACTOR extractor) {
            throw new IllegalStateException("Nothing to extract");
        }

        @Override
        public void close() {   // Nothing to close
        }
    };

    /**
     * Seeks the next occurrence of any of the characters in {@code untilOneOfChars}, or if end-of-line,
     * or even end-of-file.
     *
     * @param mark            the mutable {@link Mark} which will be updated with the findings, if any.
     * @param untilOneOfChars array of characters to seek.
     * @return {@code false} if the end was reached and hence no value found, otherwise {@code true}.
     * @throws IOException in case of I/O error.
     */
    boolean seek(Mark mark, int[] untilOneOfChars) throws IOException;

    /**
     * Extracts the value specified by the {@link Mark}, previously populated by a call to {@link #seek(Mark, int[])}.
     *
     * @param mark      the {@link Mark} specifying which part of a bigger piece of data contains the found value.
     * @param extractor {@link Extractor} capable of extracting the value.
     * @return the supplied {@link Extractor}, which after the call carries the extracted value itself, where either
     * {@link Extractor#value()} or a more specific accessor method can be called to access the value.
     */
    <EXTRACTOR extends Extractor<?>> EXTRACTOR extract(Mark mark, EXTRACTOR extractor);
}
