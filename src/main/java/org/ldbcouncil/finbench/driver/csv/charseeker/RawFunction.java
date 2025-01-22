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

/**
 * Generic function interface to map from one type to another.
 * <p/>
 * This can be used with the Iterables methods to transform lists of objects.
 */
public interface RawFunction<FROM, TO, EXCEPTION extends Exception> {
    /**
     * Apply a value to this function
     *
     * @param from the input item
     * @return the mapped item
     * @throws an exception if the function fails.
     */
    TO apply(FROM from) throws EXCEPTION;
}
