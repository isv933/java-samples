package org.isv.samples.exercises.rate.tokens.storage;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Optional;


@RequiredArgsConstructor
public class PersistentAcquireTokenSupplier<K> implements TokenAcquireSupplier<K> {
    private final PersistentRefillTokenPolicy<K> persistentRefillTokenPolicy;
    private final TokenStorage<K> tokenStorage;

    @Override
    public long acquire(K key) {

        while (true) {
            var currentTime = System.nanoTime();
            var tokensState = tokenStorage.getTokens(key);

            var result = acquire(key, tokensState, currentTime);

            if (result.isPresent()) {
                return result.get();
            }
        }
    }

    private Optional<Long> acquire(K key, TokenState storedTokensState, long currentTime) {

        var availableTokens = storedTokensState == null ? persistentRefillTokenPolicy.getInitialRefillTokens(key) :
                storedTokensState.tokensCount()
                + persistentRefillTokenPolicy.getRefillTokens(key, Duration.ofNanos(currentTime - storedTokensState.latestRefillTime()));

        var acquiredTokens = persistentRefillTokenPolicy.acquireTokens(key, availableTokens);

        if (acquiredTokens < 0) {
            throw new IllegalStateException("Acquired token cannot be negative");

        }

        if (acquiredTokens > availableTokens) {
            throw new IllegalStateException("Acquired tokens cannot be larger than available tokens");
        }

        var newTokensState = new TokenState(availableTokens - acquiredTokens, currentTime);


        return tokenStorage.tryUpdateTokens(key, storedTokensState,
                newTokensState) ? Optional.of(acquiredTokens) : Optional.empty();
    }

}
