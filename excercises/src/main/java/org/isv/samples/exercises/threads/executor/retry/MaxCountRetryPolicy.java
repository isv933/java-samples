package org.isv.samples.exercises.threads.executor.retry;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public class MaxCountRetryPolicy implements  RetryPolicy{
    private final Config config;
    private int retryCount = 0;

    @Override
    public boolean shouldRetry(Throwable ex) {
        retryCount++;

        if (retryCount < config.getMaxRetryCount()) {
            return false;
        }

        try {
            Thread.sleep(config.getRetryDelay().toMillis());
        }
        catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        return true;
    }

    @Data
    @Builder
    public static class Config {
        private final int maxRetryCount;
        private final Duration retryDelay;
    }

}
