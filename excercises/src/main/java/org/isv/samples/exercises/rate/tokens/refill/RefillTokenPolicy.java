package org.isv.samples.exercises.rate.tokens.refill;

import java.time.Duration;

public interface RefillTokenPolicy {
    long getInitialTokens();

    long getTokens(long currentTokens, Duration period);
}
