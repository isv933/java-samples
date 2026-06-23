package org.isv.samples.exercises;

import java.util.concurrent.CompletableFuture;

public interface SingleFlight<K,V> {
    CompletableFuture<V> execute(K key);
}
