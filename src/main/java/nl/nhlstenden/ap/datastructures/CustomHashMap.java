package nl.nhlstenden.ap.datastructures;

public class CustomHashMap<K, V> implements CustomMap<K, V> {

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    private Object[] buckets;
    private int size;
    private int capacity;

    public CustomHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new Object[capacity];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    private Entry<K, V> getBucket(int index) {
        return (Entry<K, V>) buckets[index];
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        if ((double) size / capacity >= LOAD_FACTOR_THRESHOLD) {
            resize();
        }

        int index = getBucketIndex(key);
        Entry<K, V> current = getBucket(index);

        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = getBucket(index);
        buckets[index] = newEntry;
        size++;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = getBucketIndex(key);
        Entry<K, V> current = getBucket(index);

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = getBucketIndex(key);
        Entry<K, V> current = getBucket(index);
        Entry<K, V> previous = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        buckets = new Object[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        size = 0;
    }

    @Override
    public CustomList<K> keys() {
        CustomList<K> keyList = new CustomLinkedList<>();
        for (int i = 0; i < capacity; i++) {
            Entry<K, V> current = getBucket(i);
            while (current != null) {
                keyList.add(current.key);
                current = current.next;
            }
        }
        return keyList;
    }

    @Override
    public CustomList<V> values() {
        CustomList<V> valueList = new CustomLinkedList<>();
        for (int i = 0; i < capacity; i++) {
            Entry<K, V> current = getBucket(i);
            while (current != null) {
                valueList.add(current.value);
                current = current.next;
            }
        }
        return valueList;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        Object[] newBuckets = new Object[newCapacity];

        for (int i = 0; i < capacity; i++) {
            Entry<K, V> current = getBucket(i);
            while (current != null) {
                Entry<K, V> next = current.next;
                int newIndex = Math.abs(current.key.hashCode()) % newCapacity;
                current.next = (Entry<K, V>) newBuckets[newIndex];
                newBuckets[newIndex] = current;
                current = next;
            }
        }

        buckets = newBuckets;
        capacity = newCapacity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (int i = 0; i < capacity; i++) {
            Entry<K, V> current = getBucket(i);
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.key).append("=").append(current.value);
                first = false;
                current = current.next;
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
