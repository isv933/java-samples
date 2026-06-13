package org.isv.samples.exercises.threads.executor.result;

import lombok.Getter;

import java.util.concurrent.CountDownLatch;

public class InternalTaskResult<T> implements TaskResult<T> {
    private final CountDownLatch readyResult = new CountDownLatch(1);
    @Getter
    private volatile T result;
    private volatile RuntimeException exception;

    public void setResult(T result) {
        this.result = result;
        readyResult.countDown();
    }

    public void setError(RuntimeException ex) {
        this.exception = ex;
        readyResult.countDown();
    }

    @Override
    public void join() {
        try {
            readyResult.await();

            if (exception != null) {
                throw exception;
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public T get() {
        join();
        return result;
    }
}
