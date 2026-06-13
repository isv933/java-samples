package org.isv.samples.exercises.rate.tokens.refill;

import lombok.RequiredArgsConstructor;
import org.isv.samples.exercises.rate.tokens.storage.TokenAcquireSupplier;

import java.time.Duration;

@RequiredArgsConstructor
public class DistributedRefillTokenPolicy<K> implements RefillTokenPolicy {
    private final K key;
    private final TokenAcquireSupplier<K> tokenSupplier;

    @Override
    public long getInitialTokens() {
        return tokenSupplier.acquire(key);
    }

    @Override
    public long getTokens(long currentTokens, Duration period) {
        return isAcquireRequired(currentTokens, period) ? tokenSupplier.acquire(key) : currentTokens;
    }

    private boolean isAcquireRequired(long currentTokens, Duration period) {
        return currentTokens <= 1 || period.compareTo(Duration.ofMinutes(1)) >= 0;
    }


}
