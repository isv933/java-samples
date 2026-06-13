package org.isv.samples.exercises;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RequiredArgsConstructor
public class LRUCacheSimple<K,V> implements LRUCache<K,V> {
    private final int maxCacheSize;
    private final LinkedList<K> cachedKeys = new LinkedList<>();
    private final HashMap<K,V> cache = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    @Override
    public V tryGet(K key) {
        lock.readLock().lock();

        try {
            return cache.get(key);
        }
        finally {
            lock.readLock().unlock();
        }
    }
    @Override
    public void put(K key,V value) {
        lock.writeLock().lock();

        try {
            while (!cachedKeys.isEmpty() && cachedKeys.size() >= maxCacheSize ) {
                    var removeKey = cachedKeys.removeLast();
                    cache.remove(removeKey);
              }
            if (!cache.containsKey(key)) {
                cachedKeys.addFirst(key);
            }
            cache.put(key,value);
        }
        finally{
            lock.writeLock().unlock();
        }
    }
}

