package org.isv.samples.exercises.rate.tokens;

import org.isv.samples.exercises.rate.RateLimiter;
import org.isv.samples.exercises.rate.tokens.refill.RefillTokenPolicy;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentTokensRateLimiter implements RateLimiter {

    private final RefillTokenPolicy refillTokenPolicy;
    private final AtomicReference<TokenState> tokensState;
    public ConcurrentTokensRateLimiter(RefillTokenPolicy refillTokenPolicy) {
        Objects.requireNonNull(refillTokenPolicy, "refillTokenPolicy should not be null");
        this.refillTokenPolicy = refillTokenPolicy;
        this.tokensState = new AtomicReference<>(createTokensState(refillTokenPolicy.getInitialTokens(), System.nanoTime()));
    }

    @Override
    public boolean tryAcquire() {
        while (true) {

            var currentTokensState = tokensState.get();
            var currentTime = System.nanoTime();
            var availableTokens = refillTokenPolicy.getTokens(currentTokensState.numTokens(),
                    Duration.ofNanos(currentTime - currentTokensState.latestRefillTime()));

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

    private TokenState createTokensState(long tokens, long refillTime) {
        return new TokenState(tokens, refillTime);
    }

    private record TokenState(long numTokens, long latestRefillTime) {
    }

}
