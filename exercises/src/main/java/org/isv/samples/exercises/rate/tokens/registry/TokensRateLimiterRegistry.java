package org.isv.samples.exercises.rate.tokens.registry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.isv.samples.exercises.rate.RateLimiter;
import org.isv.samples.exercises.rate.RateLimiterRegistry;
import org.isv.samples.exercises.rate.tokens.ConcurrentTokensRateLimiter;
import org.isv.samples.exercises.rate.tokens.refill.DistributedRefillTokenPolicy;
import org.isv.samples.exercises.rate.tokens.storage.PersistentAcquireTokenSupplier;
import org.isv.samples.exercises.rate.tokens.storage.PersistentRefillTokenPolicy;
import org.isv.samples.exercises.rate.tokens.storage.TokenStorage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TokensRateLimiterRegistry<Key> implements RateLimiterRegistry<Key> {
    //TODO: Implement LRU cache, because on this design tokens are persistent
    private final ConcurrentHashMap<Key, RateLimiter> rateLimiters = new ConcurrentHashMap<>();
    private final Function<Key, RateLimiter> rateLimiterSupplier;

    public static <Key> RateLimiterRegistry<Key> persistent(PersistentRefillTokenPolicy<Key> refillPolicy,
                                                            TokenStorage<Key> storage) {
        return new TokensRateLimiterRegistry<>(
                (key) -> new ConcurrentTokensRateLimiter(new DistributedRefillTokenPolicy<>(key,
                        new PersistentAcquireTokenSupplier<>(refillPolicy, storage))));

    }

    //TODO: implement other peristent() builders to more flexible usage

    @Override
    public RateLimiter getRateLimiter(Key key) {
        return rateLimiters.computeIfAbsent(key, rateLimiterSupplier);
    }

}
