package org.isv.samples.exercises.rate.tokens.storage;

public interface TokenAcquireSupplier<K> {
    long acquire(K key);
}
