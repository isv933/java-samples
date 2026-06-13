package org.isv.samples.exercises.threads.executor.retry;

public interface RetryPolicy {
    boolean shouldRetry(Throwable ex);
}
