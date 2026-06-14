package org.isv.samples.exercises.cache;

import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;


@RequiredArgsConstructor
public class SynchronizedLRUCache<K, V> implements Cache<K, V> {
    private final int maxCacheSize;

    private final Object lock = new Object();
    private final CacheCollection cache = new CacheCollection();

    @Override
    public V get(K key) {
        synchronized (lock) {
            return cache.get(key);
        }
    }

    @Override
    public void put(K key, V value) {
        synchronized (lock) {
            cache.put(key, value);
        }

    }

    @Override
    public void remove(K key) {
        synchronized (lock) {
            cache.remove(key);
        }
    }

    private class CacheCollection extends LinkedHashMap<K, V> {
        public CacheCollection() {
            super(16, 0.75f, true);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxCacheSize;
        }
    }

}
