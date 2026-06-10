package nl.nhlstenden.ap.datastructures;

public interface CustomMap<K, V> {

    void put(K key, V value);

    V get(K key);

    V remove(K key);

    boolean containsKey(K key);

    int size();

    boolean isEmpty();

    void clear();

    CustomList<K> keys();

    CustomList<V> values();
}
