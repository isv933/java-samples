package org.isv.samples.exercises.storage;

import java.util.concurrent.CompletableFuture;

public interface AsyncStorage<K, V> {
    CompletableFuture<V> get(K key);
}
