package org.isv.samples.exercises.stack;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public class BlockingLimitedStack<T> {

    @Data
    @Builder
    private static class StackEntry<T> {
        private final T val;
        private  final StackEntry<T> prev;
    }
    private final AtomicReference<StackEntry<T>> head = new AtomicReference<>();
    private final Semaphore freeSlots;
    private final Semaphore availableItems;

    public BlockingLimitedStack(int maxStackSize) {
        if (maxStackSize < 1 ){
            throw new IllegalArgumentException("maxStackSize cannot be less than 1");
        }

        this.freeSlots = new Semaphore(maxStackSize);
        this.availableItems = new Semaphore(0);
    }

    public void push(T value) {
        allocateOrWait();
        while(true) {
            var latest = head.get();
            var newElement = StackEntry.<T>builder().prev(latest).val(value).build();
            if (head.compareAndSet(latest, newElement)) {
                availableItems.release();
                break;
            }
        }
    }
    public T pop() {
        waitLatest();

        while (true) {
            var latest = head.get();

            if (latest == null) {
                throw new IllegalStateException("pop() not expected null value from stack");
            }

            if (head.compareAndSet(latest, latest.prev)) {
                freeSlots.release();
                return latest.val;
            }
        }
    }

    private void waitLatest() {
        try{
             availableItems.acquire();
        }
        catch(InterruptedException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private void allocateOrWait() {
        try {
          freeSlots.acquire();
        }
        catch(InterruptedException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
