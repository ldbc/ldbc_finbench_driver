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

import com.google.common.collect.Range;

public abstract class Bucket<T> {
    public abstract boolean contains(T thing);

    @Override
    public abstract boolean equals(Object obj);

    public static class NumberRangeBucket<T1 extends Number> extends Bucket<T1> {
        private final Range<Double> range;

        public NumberRangeBucket(Range<Double> range) {
            this.range = range;
        }

        @Override
        public boolean contains(T1 number) {
            return range.contains(number.doubleValue());
        }

        @Override
        public String toString() {
            return "RangeBucket [range=" + range + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((range == null) ? 0 : range.hashCode());
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
            NumberRangeBucket<T1> other = (NumberRangeBucket) obj;
            if (range == null) {
                return other.range == null;
            } else {
                return range.equals(other.range);
            }
        }

    }

    public static class DiscreteBucket<T2 extends Object> extends Bucket<T2> {
        private final T2 thing;

        public DiscreteBucket(T2 thing) {
            this.thing = thing;
        }

        public static <Type> DiscreteBucket<Type> create(Type thing) {
            return new DiscreteBucket<Type>(thing);
        }

        public T2 getId() {
            return thing;
        }

        @Override
        public boolean contains(T2 otherThing) {
            return this.thing.equals(otherThing);
        }

        @Override
        public String toString() {
            return "DiscreteBucket [thing=" + thing + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((thing == null) ? 0 : thing.hashCode());
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
            DiscreteBucket<T2> other = (DiscreteBucket) obj;
            if (thing == null) {
                return other.thing == null;
            } else {
                return thing.equals(other.thing);
            }
        }

    }
}
