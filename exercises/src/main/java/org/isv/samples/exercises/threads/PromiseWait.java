package org.isv.samples.exercises.threads;

import java.util.Objects;

public class PromiseWait<T> implements Promise<T> {
    private final Object lock = new Object();
    private State<T> completeState;

    @Override
    public void complete(T result) {
        complete(new State<>(result, null));
    }

    @Override
    public void competeException(Throwable ex) {
        Objects.requireNonNull(ex, "Exception should not be null");
        complete(new State<>(null, ex));
    }

    @Override
    public T get() {
        try {
            synchronized (lock) {
                    while(completeState == null) {
                        lock.wait();
                    }

                    if (completeState.ex()!=null){
                        throw new IllegalStateException(completeState.ex());
                    }
                    return completeState.result();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(ex);
        }
    }

    private void complete(State<T> state) {
        synchronized (lock) {

            if (completeState!=null) {
                throw new IllegalStateException("Promise is already completed");
            }

            completeState = state;
            lock.notifyAll();
        }
    }

    record State<T> (T result, Throwable ex) {

    }

}
