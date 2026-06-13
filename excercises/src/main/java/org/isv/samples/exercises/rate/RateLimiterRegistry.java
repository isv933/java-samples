package org.isv.samples.exercises.rate;

public interface RateLimiterRegistry<TKey> {
    RateLimiter getRateLimiter(TKey key);
}
