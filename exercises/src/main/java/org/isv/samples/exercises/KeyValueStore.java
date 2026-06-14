package org.isv.samples.exercises;

public interface KeyValueStore<K, V> {
    V tryGet(K key);

    void put(K key, V val);

    void delete(K key);
}
