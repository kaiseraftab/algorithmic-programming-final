package nl.nhlstenden.ap.algorithms;

import java.util.Comparator;
import nl.nhlstenden.ap.datastructures.CustomList;

public class SequentialSearch<T> implements Searcher<T> {

    @Override
    public int search(CustomList<T> list, T target, Comparator<T> comparator) {
        for (int i = 0; i < list.size(); i++) {
            if (comparator.compare(list.get(i), target) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String getName() {
        return "Sequential Search";
    }
}
