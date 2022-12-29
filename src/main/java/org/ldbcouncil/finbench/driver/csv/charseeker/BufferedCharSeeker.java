package org.ldbcouncil.finbench.driver.csv.charseeker;

import static java.lang.Math.max;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * Much like a {@link BufferedReader} for a {@link Reader}.
 */
public class BufferedCharSeeker implements CharSeeker {
    public static final char DEFAULT_QUOTE_CHAR = '"';
    private static final int KB = 1024;
    private static final int MB = KB * KB;
    public static final int DEFAULT_BUFFER_SIZE = 2 * MB;
    private static final char EOL_CHAR = '\n';
    private static final char EOL_CHAR_2 = '\r';
    private static final char EOF_CHAR = (char) -1;
    private static final char BACK_SLASH = '\\';

    private final CharReadable reader;
    private final char[] buffer;

    // Wraps the char[] buffer and is only used during reading more data, using f.ex. compact()
    // so that we don't have to duplicate that functionality.
    private final CharBuffer charBuffer;
    private final char quoteChar;
    private int bufferPos;
    private long lineStartPos;
    private int seekStartPos;
    private int lineNumber = 1;
    private boolean eof;

    public BufferedCharSeeker(CharReadable reader) {
        this(reader, DEFAULT_BUFFER_SIZE, DEFAULT_QUOTE_CHAR);
    }

    public BufferedCharSeeker(CharReadable reader, int bufferSize) {
        this(reader, bufferSize, DEFAULT_QUOTE_CHAR);
    }

    public BufferedCharSeeker(CharReadable reader, int bufferSize, char quoteChar) {
        this.reader = reader;
        this.buffer = new char[bufferSize];
        this.charBuffer = CharBuffer.wrap(buffer);
        this.bufferPos = bufferSize;
        this.quoteChar = quoteChar;
    }

    @Override
    public boolean seek(Mark mark, int[] untilOneOfChars) throws IOException {
        if (eof) {   // We're at the end
            return eof(mark);
        }

        // Keep a start position in case we need to further fill the buffer in nextChar, a value can at maximum be the
        // whole buffer, so max one fill per value is supported.
        seekStartPos = bufferPos; // seekStartPos updated in nextChar if buffer flips over, that's why it's a member
        int ch;
        int endOffset = 1;
        int skippedChars = 0;
        int quoteDepth = 0;
        while (!eof) {
            ch = nextChar(skippedChars);
            if (quoteDepth == 0) {   // In normal mode, i.e. not within quotes
                if (ch == quoteChar && seekStartPos == bufferPos
                    - 1/* -1 since we just advanced one */) {   // We found a quote, which was the first of the
                    // value, skip it and switch mode
                    quoteDepth++;
                    seekStartPos++;
                    continue;
                } else if (isNewLine(ch)) {   // Encountered newline, done for now
                    break;
                } else {
                    for (int i = 0; i < untilOneOfChars.length; i++) {
                        if (ch == untilOneOfChars[i]) {   // We found a delimiter, set marker and return true
                            mark.set(lineNumber, seekStartPos, bufferPos - endOffset - skippedChars, ch);
                            return true;
                        }
                    }
                }
            } else {   // In quoted mode, i.e. within quotes
                if (ch == quoteChar) {   // Found a quote within a quote, peek at next char
                    int nextCh = peekChar();

                    if (nextCh
                        == quoteChar) {   // Found a double quote, skip it and we're going down one more quote depth
                        // (quote-in-quote)
                        repositionChar(bufferPos++, ++skippedChars);
                        quoteDepth = quoteDepth == 1 ? 2 : 1; // toggle between quote and quote-in-quote
                    } else {   // Found an ending quote, skip it and switch mode
                        endOffset++;
                        quoteDepth--;
                    }
                } else if ((ch == EOL_CHAR || ch == EOL_CHAR_2)) {   // Found a new line, just keep going
                    nextChar(skippedChars);
                } else if (ch == BACK_SLASH) {   // Legacy concern, support java style quote encoding
                    int nextCh = peekChar();
                    if (nextCh == quoteChar) {   // Found a slash encoded quote
                        repositionChar(bufferPos++, ++skippedChars);
                    }
                }
            }
        }

        int valueLength = bufferPos - seekStartPos - 1;
        if (eof && valueLength == 0
            && seekStartPos == lineStartPos) {   // We didn't find any of the characters sought for
            return eof(mark);
        }

        // We found the last value of the line or stream
        skippedChars += skipEolChars();
        mark.set(lineNumber, seekStartPos, bufferPos - endOffset - skippedChars, Mark.END_OF_LINE_CHARACTER);
        lineNumber++;
        lineStartPos = bufferPos;
        return true;
    }

    private void repositionChar(int offset, int stepsBack) {
        // We reposition characters because we might have skipped some along the way, double-quotes and what not.
        // We want to take an as little hit as possible for that, so we reposition each character as long as
        // we're still reading the same value. All other values will not have to take any hit of skipped chars
        // for this particular value.
        buffer[offset - stepsBack] = buffer[offset];
    }

    private boolean isNewLine(int ch) {
        return ch == EOL_CHAR || ch == EOL_CHAR_2;
    }

    private int peekChar() throws IOException {
        fillBufferIfWeHaveExhaustedIt();
        return buffer[bufferPos];
    }

    private boolean eof(Mark mark) {
        mark.set(lineNumber, -1, -1, Mark.END_OF_LINE_CHARACTER);
        return false;
    }

    @Override
    public <EXTRACTOR extends Extractor<?>> EXTRACTOR extract(Mark mark, EXTRACTOR extractor) {
        long from = mark.startPosition();
        long to = mark.position();
        extractor.extract(buffer, (int) (from), (int) (to - from));
        return extractor;
    }

    private int skipEolChars() throws IOException {
        int skipped = 0;
        while (isNewLine(
            nextChar(0/*doesn't matter since we ignore the chars anyway*/))) {   // Just loop through, skipping them
            skipped++;
        }
        bufferPos--; // since nextChar advances one step
        return skipped;
    }

    private int nextChar(int skippedChars) throws IOException {
        fillBufferIfWeHaveExhaustedIt();
        int ch = buffer[bufferPos++];
        if (skippedChars > 0) {
            repositionChar(bufferPos - 1, skippedChars);
        }
        if (ch == EOF_CHAR) {
            eof = true;
        }
        return ch;
    }

    private void fillBufferIfWeHaveExhaustedIt() throws IOException {
        if (bufferPos >= buffer.length) {
            if (seekStartPos == 0) {
                throw new IllegalStateException("Tried to read in a value larger than buffer size " + buffer.length);
            }
            charBuffer.position(seekStartPos);
            charBuffer.compact();
            int remaining = charBuffer.remaining();
            int read = reader.read(buffer, charBuffer.position(), remaining);
            if (read < remaining) {
                buffer[charBuffer.position() + max(read, 0)] = EOF_CHAR;
            }
            bufferPos = charBuffer.position();
            seekStartPos = 0;
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[buffer:" + charBuffer + ", seekPos:" + seekStartPos + ", line:"
            + lineNumber + "]";
    }
}
