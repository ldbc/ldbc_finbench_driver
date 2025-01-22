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

public class Tuple3<T1, T2, T3> {
    private final T1 thing1;
    private final T2 thing2;
    private final T3 thing3;

    public Tuple3(T1 t1, T2 t2, T3 t3) {
        thing1 = t1;
        thing2 = t2;
        thing3 = t3;
    }

    public T1 _1() {
        return thing1;
    }

    public T2 _2() {
        return thing2;
    }

    public T3 _3() {
        return thing3;
    }

    @Override
    public String toString() {
        return "Triple [thing1=" + thing1 + ", thing2=" + thing2 + ", thing3=" + thing3 + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((thing1 == null) ? 0 : thing1.hashCode());
        result = prime * result + ((thing2 == null) ? 0 : thing2.hashCode());
        result = prime * result + ((thing3 == null) ? 0 : thing3.hashCode());
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
        Tuple3 other = (Tuple3) obj;
        if (thing1 == null) {
            if (other.thing1 != null) {
                return false;
            }
        } else if (!thing1.equals(other.thing1)) {
            return false;
        }
        if (thing2 == null) {
            if (other.thing2 != null) {
                return false;
            }
        } else if (!thing2.equals(other.thing2)) {
            return false;
        }
        if (thing3 == null) {
            return other.thing3 == null;
        } else {
            return thing3.equals(other.thing3);
        }
    }
}
