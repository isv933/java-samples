package org.isv.samples.exercises.queue;

import lombok.RequiredArgsConstructor;

import java.util.ArrayDeque;
import java.util.Deque;


@RequiredArgsConstructor
public class BoundedBlockedQueueWithWaitNotify<V> implements Queue<V> {
    private final int maxQueueSize;
    private final Deque<V> queue = new ArrayDeque<>();
    private final Object lock = new Object();

    public V take() {

        try {
            synchronized (lock) {
                while (queue.isEmpty()) {
                    lock.wait();
                }

                lock.notifyAll();
                return queue.removeFirst();
            }
        } catch (InterruptedException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public void put(V value) {
        try {
            synchronized (lock) {
                while (queue.size() >= maxQueueSize) {
                    lock.wait();
                }
                queue.addLast(value);

                lock.notifyAll();
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
