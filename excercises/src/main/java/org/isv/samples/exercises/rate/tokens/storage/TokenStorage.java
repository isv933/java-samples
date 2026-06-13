package org.isv.samples.exercises.rate.tokens.storage;

public interface TokenStorage<K> {

    TokenState getTokens(K key);

    boolean tryUpdateTokens(K key, TokenState oldTokenState, TokenState newTokenState);

}
