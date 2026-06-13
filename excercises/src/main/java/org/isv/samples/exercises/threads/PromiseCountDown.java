package org.isv.samples.exercises.threads;

import java.util.concurrent.CountDownLatch;

public class PromiseCountDown<T> implements Promise<T> {

    private final CountDownLatch latch = new CountDownLatch(1);
    private T result;
    private Throwable exception;

    @Override
    public void complete(T result) {
        this.result = result;
        complete();
    }

    @Override
    public void competeException(Throwable ex) {
        this.exception = ex;
        complete();
    }

    @Override
    public T get() {
        try {
            latch.await();
            if (result != null) {
                return result;
            }
            throw new IllegalStateException(exception);
        } catch (InterruptedException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private void complete() {
        latch.countDown();
    }
}
