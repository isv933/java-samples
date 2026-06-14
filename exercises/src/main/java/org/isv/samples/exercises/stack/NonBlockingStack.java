package org.isv.samples.exercises.stack;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.atomic.AtomicReference;

public class NonBlockingStack<T> {
    private final AtomicReference<Element<T>> stackHead = new AtomicReference<>(null);

    public void push(T value) {
        while (true) {
            var latest = stackHead.get();
            var newElement = Element.<T>builder().value(value).prev(latest).build();
            if (stackHead.compareAndSet(latest, newElement)) {
                break;
            }
        }
    }

    public T pop() {
        while (true) {
            var latest = stackHead.get();
            if (latest == null) {
                throw new RuntimeException("Stack is empty");
            }
            if (stackHead.compareAndSet(latest, latest.prev)) {
                return latest.value;
            }
        }
    }

    @Data
    @Builder
    private static class Element<T> {
        private final T value;
        private final Element<T> prev;
    }


}
