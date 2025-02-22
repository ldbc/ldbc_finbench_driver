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

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Means of instantiating common {@link CharReadable} instances.
 * <p/>
 * There are support for compressed files as well for those methods accepting a {@link File} argument.
 * <ol>
 * <li>ZIP: is both an archive and a compression format. In many cases the order of files
 * is important and for a ZIP archive with multiple files, the order of the files are whatever the order
 * set by the tool that created the ZIP archive. Therefore only single-file-zip files are supported.
 * The single file in the given ZIP archive will be decompressed on the fly, while reading.</li>
 * <li>GZIP: is only a compression format and so will be decompressed on the fly, while reading.</li>
 * </ol>
 */
public class Readables {
    public static final CharReadable EMPTY = new CharReadable() {
        @Override
        public int read(char[] buffer, int offset, int length) throws IOException {
            return -1;
        }

        @Override
        public void close() throws IOException {   // Nothing to close
        }
    };
    /**
     * First 4 bytes of a ZIP file have this signature.
     */
    private static final int ZIP_MAGIC = 0x504b0304;
    /**
     * First 2 bytes of a GZIP file have this signature.
     */
    private static final int GZIP_MAGIC = 0x1f8b;
    private static final RawFunction<File, CharReadable, IOException> FROM_FILE =
        new RawFunction<File, CharReadable, IOException>() {
            @Override
            public CharReadable apply(File file) throws IOException {
                int magic = magic(file);
                if (magic == ZIP_MAGIC) {   // ZIP file
                    ZipFile zipFile = new ZipFile(file);
                    ZipEntry entry = getSingleSuitableEntry(zipFile);
                    return wrap(new InputStreamReader(zipFile.getInputStream(entry)));
                } else if ((magic >>> 16)
                    == GZIP_MAGIC) {   // GZIP file. GZIP isn't an archive like ZIP, so this is purely data that is
                    // compressed.
                    // Although a very common way of compressing with GZIP is to use TAR which can combine many
                    // files into one blob, which is then compressed. If that's the case then
                    // the data will look like garbage and the reader will fail for whatever it will be used for.
                    // TODO add tar support
                    GZIPInputStream zipStream = new GZIPInputStream(new FileInputStream(file));
                    return wrap(new InputStreamReader(zipStream));
                }

                return wrap(new FileReader(file));
            }

            private ZipEntry getSingleSuitableEntry(ZipFile zipFile) throws IOException {
                List<String> unsuitableEntries = new ArrayList<>();
                Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
                ZipEntry found = null;
                while (enumeration.hasMoreElements()) {
                    ZipEntry entry = enumeration.nextElement();
                    if (entry.isDirectory() || invalidZipEntry(entry.getName())) {
                        unsuitableEntries.add(entry.getName());
                        continue;
                    }

                    if (found != null) {
                        throw new IOException(
                            "Multiple suitable files found in zip file " + zipFile.getName() + ", at least "
                                + found.getName() + " and " + entry.getName()
                                + ". Only a single file per zip file is supported");
                    }
                    found = entry;
                }

                if (found == null) {
                    throw new IOException(
                        "No suitable file found in zip file " + zipFile.getName() + "." + (!unsuitableEntries.isEmpty()
                            ? " Although found these unsuitable entries " + unsuitableEntries : ""));
                }
                return found;
            }

            private int magic(File file) throws IOException {
                try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
                    return in.readInt();
                } catch (EOFException e) {
                    return -1;
                }
            }
        };
    private static final RawFunction<CharReadable, CharReadable, IOException> IDENTITY =
        new RawFunction<CharReadable, CharReadable, IOException>() {
            @Override
            public CharReadable apply(CharReadable in) {
                return in;
            }
        };

    private Readables() {
        throw new AssertionError("No instances allowed");
    }

    public static CharReadable wrap(final Reader reader) {
        return new CharReadable() {
            @Override
            public int read(char[] buffer, int offset, int length) throws IOException {
                return reader.read(buffer, offset, length);
            }

            @Override
            public void close() throws IOException {
                reader.close();
            }
        };
    }

    private static boolean invalidZipEntry(String name) {
        return name.contains("__MACOSX") || name.startsWith(".") || name.contains("/.");
    }

    public static CharReadable file(File file) throws IOException {
        return FROM_FILE.apply(file);
    }

    public static CharReadable multipleFiles(File... files) {
        return new MultiReadable(iterator(files, FROM_FILE));
    }

    public static CharReadable multipleFiles(Iterator<File> files) {
        return new MultiReadable(iterator(files, FROM_FILE));
    }

    public static CharReadable multipleSources(CharReadable... sources) {
        return new MultiReadable(iterator(sources, IDENTITY));
    }

    public static CharReadable multipleSources(RawIterator<CharReadable, IOException> sources) {
        return new MultiReadable(sources);
    }

    private static <IN, OUT> RawIterator<OUT, IOException> iterator(final Iterator<IN> items,
                                                                    final RawFunction<IN, OUT, IOException> converter) {
        return new RawIterator<OUT, IOException>() {
            @Override
            public boolean hasNext() {
                return items.hasNext();
            }

            @Override
            public OUT next() throws IOException {
                return converter.apply(items.next());
            }

            @Override
            public void remove() {
                items.remove();
            }
        };
    }

    private static <IN, OUT> RawIterator<OUT, IOException> iterator(final IN[] items,
                                                                    final RawFunction<IN, OUT, IOException> converter) {
        if (items.length == 0) {
            throw new IllegalStateException("No source items specified");
        }

        return new RawIterator<OUT, IOException>() {
            private int cursor;

            @Override
            public boolean hasNext() {
                return cursor < items.length;
            }

            @Override
            public OUT next() throws IOException {
                if (!hasNext()) {
                    throw new IllegalStateException();
                }
                return converter.apply(items[cursor++]);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
