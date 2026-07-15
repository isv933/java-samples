package org.isv.samples.exercises.circuit;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class CircuitBreaker {
    private final int successThreshold;
    private final int failThreshold;
    private final Duration timeoutDuration;

    enum State {
        CLOSE,
        HALF_OPEN,
        OPEN
    }
    private final AtomicReference<State> state  = new AtomicReference<>(State.CLOSE);
    private final AtomicInteger successCount = new AtomicInteger();
    private final AtomicInteger failureCount =  new AtomicInteger();;
    private volatile long latestFailCallTimeNs;

    public <T> T execute(Supplier<T> executor) {
        return handleExecute(executor);
    }

    public boolean canExecute(){

        handleOpen();
        return state.get()!=State.OPEN;
    }


    private <T> T handleExecute(Supplier<T> executor) {
        handleOpen();

        if (state.get()==State.OPEN) {
            throw new CircuitBreakerException();
        }

        try {
            return handleSuccess(executor.get());
        }
        catch(RuntimeException ex) {
            handleException();
            throw ex;
        }
    }

    private void handleOpen(){
        updateState(s->s == State.OPEN && System.nanoTime() - latestFailCallTimeNs > timeoutDuration.toNanos(),
                State.HALF_OPEN,()->successCount.set(0));
    }

    private <T> T handleSuccess(T result){

        if (state.get() == State.HALF_OPEN) {
            var success = successCount.incrementAndGet();
            updateState(s->s==State.HALF_OPEN && success >= successThreshold, State.CLOSE,()->{});
        }
        return result;
    }

    private void handleException(){
        var failed = failureCount.incrementAndGet();
        latestFailCallTimeNs = System.nanoTime();

        updateState(s->(s == State.CLOSE && failed >= failThreshold) || s == State.HALF_OPEN,
                State.OPEN, ()->failureCount.set(0));

    }

    private void updateState(Predicate<State> condition, State resultState, Runnable success) {
        while (true) {
            var localState = state.get();
            if (condition.test(localState)) {
                if (state.compareAndSet(localState, resultState)) {
                    success.run();
                    break;
                }
            } else {
                break;
            }
        }
    }

}


