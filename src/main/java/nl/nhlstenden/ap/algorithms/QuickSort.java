package nl.nhlstenden.ap.algorithms;

import java.util.Comparator;
import nl.nhlstenden.ap.datastructures.CustomList;

public class QuickSort<T> implements Sorter<T> {

    @Override
    public void sort(CustomList<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) {
            return;
        }
        quickSort(list, 0, list.size() - 1, comparator);
    }

    private void quickSort(CustomList<T> list, int low, int high, Comparator<T> comparator) {
        while (low < high) {
            int pivotIndex = partition(list, low, high, comparator);
            if (pivotIndex - low < high - pivotIndex) {
                quickSort(list, low, pivotIndex - 1, comparator);
                low = pivotIndex + 1;
            } else {
                quickSort(list, pivotIndex + 1, high, comparator);
                high = pivotIndex - 1;
            }
        }
    }

    private int partition(CustomList<T> list, int low, int high, Comparator<T> comparator) {
        int mid = low + (high - low) / 2;
        int median = medianOfThree(list, low, mid, high, comparator);
        swap(list, median, high);

        T pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                swap(list, i, j);
            }
        }

        swap(list, i + 1, high);
        return i + 1;
    }

    private int medianOfThree(CustomList<T> list, int a, int b, int c, Comparator<T> comparator) {
        T va = list.get(a);
        T vb = list.get(b);
        T vc = list.get(c);

        if (comparator.compare(va, vb) <= 0) {
            if (comparator.compare(vb, vc) <= 0) {
                return b;
            }
            return comparator.compare(va, vc) <= 0 ? c : a;
        } else {
            if (comparator.compare(va, vc) <= 0) {
                return a;
            }
            return comparator.compare(vb, vc) <= 0 ? c : b;
        }
    }

    private void swap(CustomList<T> list, int a, int b) {
        T temp = list.get(a);
        list.set(a, list.get(b));
        list.set(b, temp);
    }

    @Override
    public String getName() {
        return "Quick Sort";
    }
}
