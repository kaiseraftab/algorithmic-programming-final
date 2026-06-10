package nl.nhlstenden.ap.datastructures;

public interface CustomTree<T extends Comparable<T>> {

    void insert(T element);

    boolean search(T element);

    T remove(T element);

    int size();

    boolean isEmpty();

    void clear();

    CustomList<T> inOrder();

    CustomList<T> preOrder();

    CustomList<T> postOrder();
}
