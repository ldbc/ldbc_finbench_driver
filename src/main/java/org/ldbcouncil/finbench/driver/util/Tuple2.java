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

package org.ldbcouncil.finbench.driver.util;

public class Tuple2<T1, T2> {
    private final T1 thing1;
    private final T2 thing2;

    public Tuple2(T1 t1, T2 t2) {
        thing1 = t1;
        thing2 = t2;
    }

    public T1 _1() {
        return thing1;
    }

    public T2 _2() {
        return thing2;
    }

    @Override
    public String toString() {
        return "Pair [thing1=" + thing1 + ", thing2=" + thing2 + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((thing1 == null) ? 0 : thing1.hashCode());
        result = prime * result + ((thing2 == null) ? 0 : thing2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Tuple2 other = (Tuple2) obj;
        if (thing1 == null) {
            if (other.thing1 != null) {
                return false;
            }
        } else if (!thing1.equals(other.thing1)) {
            return false;
        }
        if (thing2 == null) {
            return other.thing2 == null;
        } else {
            return thing2.equals(other.thing2);
        }
    }

}
