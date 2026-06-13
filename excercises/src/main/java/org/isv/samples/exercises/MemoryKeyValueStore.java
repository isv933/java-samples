package org.isv.samples.exercises;

import lombok.Builder;
import lombok.Data;

import java.io.Closeable;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryKeyValueStore<K, V> implements KeyValueStore<K, V>, Closeable {
    private final Duration ttl;
    private final int maxCapacity;
    private final ScheduledExecutorService executorService;
    private final Future<?> cleanupTask;
    private final ConcurrentHashMap<K, MemoryValue<V>> store = new ConcurrentHashMap<>();
    private final Object storeFullLock = new Object();
    private final AtomicLong latestTtlScanTime = new AtomicLong();


    public MemoryKeyValueStore(Duration ttl, int maxCapacity) {
        this.ttl = ttl;
        this.maxCapacity = maxCapacity;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.cleanupTask = executorService.scheduleAtFixedRate(this::cleanupTask, 100, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void close() {
        executorService.shutdown();
    }

    @Override
    public V tryGet(K key) {
        var result = store.computeIfPresent(key, (k, v) -> isValueExpired(v) ? null : v);
        return result != null ? result.getValue() : null;
    }

    @Override
    public void put(K key, V val) {
        ensureCapacity();
        store.put(key, new MemoryValue<>(val, System.nanoTime()));
    }

    @Override
    public void delete(K key) {
        store.remove(key);
    }

    private void ensureCapacity() {
        try {
            if (store.size() >= maxCapacity) {
                synchronized (storeFullLock) {
                    while (store.size() >= maxCapacity) {
                        storeFullLock.wait();
                    }
                }
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void cleanupTask() {
        if (System.nanoTime() - latestTtlScanTime.get() < ttl.toNanos() || store.size() < maxCapacity) {
            return;
        }

        store.forEach((k, v) -> {
            if (isValueExpired(v)) {
                store.remove(k, v);
            }
        });

        synchronized (storeFullLock) {
            if (store.size() < maxCapacity) {
                storeFullLock.notifyAll();
            }
        }
        latestTtlScanTime.set(System.nanoTime());
    }

    private boolean isValueExpired(MemoryValue<V> value) {
        return System.nanoTime() - value.getTimestamp() >= ttl.toNanos();
    }

    @Builder
    @Data
    private static class MemoryValue<V> {
        private final V value;
        private final long timestamp;
    }

}
