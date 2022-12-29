package org.ldbcouncil.finbench.driver.csv.charseeker;

/**
 * Extracts a value from a part of a {@code char[]} into any type of value, f.ex. a {@link Extractors#string()}
 * string},
 * {@link Extractors#long_()}  long} or {@link Extractors#intArray()}.
 * <p/>
 * An {@link Extractor} is mutable for the single purpose of ability to reuse its value instance. Consider extracting
 * a primitive int -
 * <p/>
 * Sub-interfaces and implementations can and should specify specific accessors for the purpose
 * of performance and less garbage, f.ex. where an IntExtractor could have an accessor method for
 * getting the extracted value as primitive int, to avoid auto-boxing which would arise from calling {@link #value()}.
 *
 * @see Extractors for a collection of very common extractors.
 */
public interface Extractor<T> {
    void extract(char[] data, int offset, int length);

    T value();

    /**
     * @return string representation of what type of value of produces. Also used as key in {@link Extractors}.
     */
    @Override
    String toString();
}
