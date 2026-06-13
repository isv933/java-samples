package org.isv.samples.exercises.queue;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;

public class BoundedBlockedQueueWithSemaphore<V> implements Queue<V> {

    private final Semaphore freeItems;
    private final Semaphore usedItems;
    private final Deque<V> queue = new ConcurrentLinkedDeque<>();

    public BoundedBlockedQueueWithSemaphore(int maxQueueSize) {
        this.freeItems = new Semaphore(maxQueueSize);
        this.usedItems = new Semaphore(0);
    }

    @Override
    public V take() {

        try {
            usedItems.acquire();
            var res  = queue.removeFirst();
            freeItems.release();
            return res;
        }
        catch(InterruptedException ex){
            throw new IllegalStateException(ex);

        }
    }

    @Override
    public void put(V value) {
        try {
            freeItems.acquire();
            queue.add(value);
            usedItems.release();
        }
        catch(InterruptedException ex){
            throw new IllegalStateException(ex);

        }
    }
}
