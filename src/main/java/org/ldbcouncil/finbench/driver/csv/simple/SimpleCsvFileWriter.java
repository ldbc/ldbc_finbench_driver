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

package org.ldbcouncil.finbench.driver.csv.simple;

import com.google.common.base.Charsets;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

public class SimpleCsvFileWriter implements Closeable {
    public static final String DEFAULT_COLUMN_SEPARATOR = "|";

    private final BufferedWriter bufferedWriter;
    private final String columnSeparator;
    private final boolean flushLog;

    public SimpleCsvFileWriter(File file, String columnSeparator, boolean flushLog) throws IOException {
        this.bufferedWriter =
            new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8));

        this.columnSeparator = columnSeparator;
        this.flushLog = flushLog;
    }

    public void writeRows(Iterator<String[]> csvRows) throws IOException {
        while (csvRows.hasNext()) {
            writeRow(csvRows.next());
        }
    }

    public void writeRow(String... columns) throws IOException {
        for (int i = 0; i < columns.length - 1; i++) {
            bufferedWriter.write(columns[i]);
            bufferedWriter.write(columnSeparator);
        }
        bufferedWriter.write(columns[columns.length - 1]);
        bufferedWriter.newLine();
        if (flushLog) {
            bufferedWriter.flush();
        }
    }

    @Override
    public void close() throws IOException {
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
