package org.isv.samples.exercises.rate.tokens.storage;

import java.time.Duration;

public interface PersistentRefillTokenPolicy<K> {
    long getInitialRefillTokens(K key);
    long getRefillTokens(K key, Duration period);
    long acquireTokens(K key, long totalTokens);
}
