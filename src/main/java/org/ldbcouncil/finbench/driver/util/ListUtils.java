package org.ldbcouncil.finbench.driver.util;

import com.google.common.collect.Lists;
import java.util.HashSet;
import java.util.List;

public class ListUtils {

    /**
     * Check if two unordered lists are equal
     *
     * @param <T> The object type in the Iterable
     * @param a   First list for comparison
     * @param b   Second list for comparison
     * @return True is equal, otherwise false
     */
    public static <T extends Object> boolean listsEqual(Iterable<T> a, Iterable<T> b) {
        if (a == null && b == null) {
            return true;
        } else if (a == null || b == null) {
            return false;
        }

        List<T> listA = Lists.newArrayList(a);
        List<T> listB = Lists.newArrayList(b);
        if (listA.size() != listB.size()) {
            return false;
        }
        HashSet<T> setA = new HashSet<>();
        setA.addAll(listA);
        HashSet<T> setB = new HashSet<>();
        setB.addAll(listB);

        return setA.equals(setB);
    }
}
