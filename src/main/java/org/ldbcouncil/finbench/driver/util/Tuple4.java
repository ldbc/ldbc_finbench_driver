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

import java.util.Objects;

public class Tuple4<T1, T2, T3, T4> {
    private final T1 thing1;
    private final T2 thing2;
    private final T3 thing3;
    private final T4 thing4;

    public Tuple4(T1 thing1, T2 thing2, T3 thing3, T4 thing4) {
        this.thing1 = thing1;
        this.thing2 = thing2;
        this.thing3 = thing3;
        this.thing4 = thing4;
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

    public T4 _4() {
        return thing4;
    }

    @Override
    public String toString() {
        return "Tuple4{" + "thing1=" + thing1 + ", thing2="
            + thing2 + ", thing3=" + thing3 + ", thing4=" + thing4 + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tuple4<?, ?, ?, ?> tuple4 = (Tuple4<?, ?, ?, ?>) o;
        return Objects.equals(thing1, tuple4.thing1) && Objects.equals(thing2, tuple4.thing2)
            && Objects.equals(thing3, tuple4.thing3) && Objects.equals(thing4, tuple4.thing4);
    }

    @Override
    public int hashCode() {
        return Objects.hash(thing1, thing2, thing3, thing4);
    }
}
