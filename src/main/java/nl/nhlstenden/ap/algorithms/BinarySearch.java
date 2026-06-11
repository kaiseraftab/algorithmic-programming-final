package nl.nhlstenden.ap.algorithms;

import java.util.Comparator;
import nl.nhlstenden.ap.datastructures.CustomList;

public class BinarySearch<T> implements Searcher<T> {

    @Override
    public int search(CustomList<T> list, T target, Comparator<T> comparator) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = comparator.compare(list.get(mid), target);

            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }

    @Override
    public String getName() {
        return "Binary Search";
    }
}
