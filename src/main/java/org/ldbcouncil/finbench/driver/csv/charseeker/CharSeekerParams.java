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

public class CharSeekerParams {
    private final int bufferSize;
    private final char columnDelimiter;
    private final char arrayDelimiter;
    private final char tupleDelimiter;

    public CharSeekerParams(int bufferSize, char columnDelimiter, char arrayDelimiter, char tupleDelimiter) {
        this.bufferSize = bufferSize;
        this.columnDelimiter = columnDelimiter;
        this.arrayDelimiter = arrayDelimiter;
        this.tupleDelimiter = tupleDelimiter;
    }

    public int bufferSize() {
        return bufferSize;
    }

    public char columnDelimiter() {
        return columnDelimiter;
    }

    public char arrayDelimiter() {
        return arrayDelimiter;
    }

    public char tupleDelimiter() {
        return tupleDelimiter;
    }
}
