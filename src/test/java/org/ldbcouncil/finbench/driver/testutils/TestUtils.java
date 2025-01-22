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

package org.ldbcouncil.finbench.driver.testutils;

import static java.lang.String.format;

import java.io.File;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
import org.ldbcouncil.finbench.driver.temporal.TimeSource;

public class TestUtils {
    public static File getResource(String path) {
        return FileUtils.toFile(TestUtils.class.getResource(path));
    }

    public static ThreadPoolLoadGenerator newThreadPoolLoadGenerator(int threadCount, long sleepDurationAsMilli) {
        return new ThreadPoolLoadGenerator(threadCount, sleepDurationAsMilli);
    }

    public static <T> boolean generateBeforeTimeout(
        Iterator<T> generator,
        long timeoutAsMilli,
        TimeSource timeSource,
        long itemsToGenerate) {
        long startTimeAsMilli = timeSource.nowAsMilli();
        long itemsGenerated = 0;
        while (generator.hasNext()) {
            generator.next();
            itemsGenerated++;
            // check if generated enough items yet
            if (itemsGenerated >= itemsToGenerate) {
                break;
            }
            // occasionally check for timeout
            if ((itemsGenerated % 1000 == 0) && (timeSource.nowAsMilli() > timeoutAsMilli)) {
                break;
            }
        }
        long finishTimeAsMilli = timeSource.nowAsMilli();
        boolean result = (finishTimeAsMilli < timeoutAsMilli)
            && (itemsGenerated >= itemsToGenerate || false == generator.hasNext());
        long testDurationAsMilli = finishTimeAsMilli - startTimeAsMilli;
        System.out.println(
            format("Generated %s elements in %s ms ---> %s elements/ms",
                itemsGenerated,
                testDurationAsMilli,
                (double) itemsGenerated
                    / testDurationAsMilli));
        return result;
    }
}
