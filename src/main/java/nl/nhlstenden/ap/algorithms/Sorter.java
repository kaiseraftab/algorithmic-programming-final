package nl.nhlstenden.ap.algorithms;

import java.util.Comparator;
import nl.nhlstenden.ap.datastructures.CustomList;

public interface Sorter<T> {

    void sort(CustomList<T> list, Comparator<T> comparator);

    @SuppressWarnings("unchecked")
    default void sort(CustomList<T> list) {
        sort(list, (a, b) -> ((Comparable<? super T>) a).compareTo(b));
    }

    String getName();
}
