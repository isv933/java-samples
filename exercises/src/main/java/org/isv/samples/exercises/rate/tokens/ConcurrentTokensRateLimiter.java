package org.isv.samples.exercises.rate.tokens;

import org.isv.samples.exercises.rate.RateLimiter;
import org.isv.samples.exercises.rate.tokens.refill.RefillTokenPolicy;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentTokensRateLimiter implements RateLimiter {

    private final RefillTokenPolicy refillTokenPolicy;
    private final AtomicReference<TokenState> tokensState;

    public ConcurrentTokensRateLimiter(RefillTokenPolicy refillTokenPolicy) {
        Objects.requireNonNull(refillTokenPolicy, "refillTokenPolicy should not be null");
        this.refillTokenPolicy = refillTokenPolicy;
        this.tokensState = new AtomicReference<>(createTokensState(refillTokenPolicy.getInitialTokens(), Instant.now()));
    }

    @Override
    public boolean tryAcquire() {
        while (true) {

            var currentTokensState = tokensState.get();
            var currentTime = Instant.now();
            var availableTokens = refillTokenPolicy.getTokens(currentTokensState.numTokens(),
                    Duration.between(currentTime, currentTokensState.latestRefillTime()));

            if (availableTokens < 1) {
                return false;
            }

            if (tokensState.compareAndSet(currentTokensState,
                    createTokensState(availableTokens - 1,
                            availableTokens == currentTokensState.numTokens() ?
                                    currentTime : currentTokensState.latestRefillTime()))) {

                return true;
            }

        }
    }

    private TokenState createTokensState(long tokens, Instant refillTime) {
        return new TokenState(tokens, refillTime);
    }

    private record TokenState(long numTokens, Instant latestRefillTime) {
    }

}
