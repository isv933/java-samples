package org.isv.samples.exercises.rate.tokens.refill;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Objects;

@RequiredArgsConstructor
public class OneProcessRefillTokenPolicy implements RefillTokenPolicy {
    private final InitialTokenPolicy initialTokenPolicy;
    private final TokenPolicy tokenPolicy;
    private final int maxTokens;
    private final double tokensRate;

    public OneProcessRefillTokenPolicy(InitialTokenPolicy initialTokenPolicy, TokenPolicy tokenPolicy, int maxTokens) {

        Objects.requireNonNull(initialTokenPolicy, "InitialTokensPolicy should not be null");

        if (tokenPolicy == null || isInvalidTokenPolicy(tokenPolicy)) {
            throw new IllegalArgumentException("Token refill policy is not valid");
        }

        if (maxTokens <= 0) {
            throw new IllegalArgumentException("maxTokens should be greater than 0");
        }

        this.initialTokenPolicy = initialTokenPolicy;
        this.tokenPolicy = tokenPolicy;
        this.maxTokens = maxTokens;
        this.tokensRate = (double) tokenPolicy.numTokenInPeriod() / tokenPolicy.period().toNanos();
    }

    @Override
    public long getInitialTokens() {
        return switch (initialTokenPolicy) {
            case EMPTY -> 0;
            case FULL -> maxTokens;
            case ONE_PERIOD -> tokenPolicy.numTokenInPeriod();
        };
    }

    @Override
    public long getTokens(long currentTokens, Duration period) {
        return Math.min(maxTokens, (long) (period.toNanos() * tokensRate));
    }

    private boolean isInvalidTokenPolicy(TokenPolicy tokenPolicy) {
        return tokenPolicy.period() == null || tokenPolicy.period().isNegative() || tokenPolicy.period().isZero() || tokenPolicy.numTokenInPeriod() <= 0;
    }

    public enum InitialTokenPolicy {
        EMPTY,
        FULL,
        ONE_PERIOD
    }

    public record TokenPolicy(Duration period, int numTokenInPeriod) {

    }
}
