package org.isv.samples.exercises.rate.tokens.storage;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;


@RequiredArgsConstructor
public class InMemoryTokenStorage<K> implements TokenStorage<K> {

    private final ConcurrentHashMap<K, TokenState> tokenStorage = new ConcurrentHashMap<>();

    @Override
    public TokenState getTokens(K key) {
        return tokenStorage.get(key);
    }

    @Override
    public boolean tryUpdateTokens(K key, TokenState oldTokenState, TokenState newTokenState) {

        var resultTokenState =
                oldTokenState == null ?
                        tokenStorage.computeIfAbsent(key, (k) -> newTokenState) :
                        tokenStorage.computeIfPresent(key, (k, v) ->
                                                           v.equals(oldTokenState) ? newTokenState : v);

        return newTokenState.equals(resultTokenState);
    }
}
