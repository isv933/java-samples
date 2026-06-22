package org.isv.samples.exercises.threads;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class PromiseCountDown<T> implements Promise<T> {

    private final CountDownLatch latch = new CountDownLatch(1);
    private final AtomicReference<State<T>> completeState = new AtomicReference<>();

    @Override
    public void complete(T result) {
        complete(new State<>(result,null));
    }

    @Override
    public void competeException(Throwable ex) {
        complete(new State<>(null, ex));
    }

    @Override
    public T get() {
        try {
            latch.await();
            var state = this.completeState.get();

            if (state.ex()!=null) {
                throw new IllegalStateException(state.ex());
            }
            return state.result();

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(ex);
        }
    }

    private void complete(State<T> state) {
        if (!this.completeState.compareAndSet(null, state)){
            throw new IllegalStateException("Promise is already completed");
        }
       latch.countDown();
    }

    record State<T> (T result, Throwable ex)  {

    }
}
