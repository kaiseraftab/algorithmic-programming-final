package nl.nhlstenden.ap.algorithms;

import java.util.Comparator;
import nl.nhlstenden.ap.datastructures.CustomList;

public interface Searcher<T> {

    int search(CustomList<T> list, T target, Comparator<T> comparator);

    @SuppressWarnings("unchecked")
    default int search(CustomList<T> list, T target) {
        return search(list, target, (a, b) -> ((Comparable<? super T>) a).compareTo(b));
    }

    String getName();
}
