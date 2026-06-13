package org.isv.samples.exercises.threads.executor.retry;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RetryExecutor {
    private final Duration maxTerminateTimeout;
    private final ExecutorService taskExecutor;

    public RetryExecutor(int maxThreads, Duration maxTerminateTimeout){

        if (maxThreads < 1){
            throw new IllegalArgumentException("maxThreads should be greater then 0");
        }

        if (maxTerminateTimeout==null || maxTerminateTimeout.isZero() || maxTerminateTimeout.isNegative()) {
            throw new IllegalArgumentException("maxTerminateTimeout should be not null, zero or negative");
        }

        this.maxTerminateTimeout = maxTerminateTimeout;
        this.taskExecutor = Executors.newFixedThreadPool(maxThreads);
    }

    public <T> CompletableFuture<T> executeAsync(Supplier<T> task, RetryPolicy retryPolicy) {
        var result = new CompletableFuture<T>();
        taskExecutor.execute(()->runTask(task, result, retryPolicy));
        return result;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shutdown(){
        taskExecutor.shutdownNow();
        try {
            taskExecutor.awaitTermination(maxTerminateTimeout.toNanos(), TimeUnit.NANOSECONDS);
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private <T> void runTask(Supplier<T> task, CompletableFuture<T> result, RetryPolicy retryPolicy) {

        while (true) {
            try {
                result.complete(task.get());
                break;
            } catch (Throwable ex) {
                if (!retryPolicy.shouldRetry(ex)) {
                    result.completeExceptionally(ex);
                    break;
                }
            }
        }
    }
}
