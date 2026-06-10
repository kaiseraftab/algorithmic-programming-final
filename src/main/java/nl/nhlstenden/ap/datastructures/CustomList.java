package nl.nhlstenden.ap.datastructures;

public interface CustomList<T> extends Iterable<T> {

    void add(T element);

    void add(int index, T element);

    T get(int index);

    void set(int index, T element);

    T remove(int index);

    boolean contains(T element);

    int indexOf(T element);

    int size();

    boolean isEmpty();

    void clear();
}
