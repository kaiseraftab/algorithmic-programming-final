package nl.nhlstenden.ap.algorithms;

import java.util.Comparator;
import nl.nhlstenden.ap.datastructures.CustomList;

public class InsertionSort<T> implements Sorter<T> {

    @Override
    public void sort(CustomList<T> list, Comparator<T> comparator) {
        for (int i = 1; i < list.size(); i++) {
            T key = list.get(i);
            int j = i - 1;

            while (j >= 0 && comparator.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }

            list.set(j + 1, key);
        }
    }

    @Override
    public String getName() {
        return "Insertion Sort";
    }
}
