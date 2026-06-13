package org.isv.samples.exercises;

public interface LRUCache<K, V> {
    V tryGet(K key);

    void put(K key, V value);
}
