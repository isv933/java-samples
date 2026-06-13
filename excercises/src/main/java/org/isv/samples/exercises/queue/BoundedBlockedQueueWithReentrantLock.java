package org.isv.samples.exercises.queue;

import lombok.RequiredArgsConstructor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
public class BoundedBlockedQueueWithReentrantLock<V> implements Queue<V> {

    private final int maxQueueSize;
    private final Deque<V> queue = new ArrayDeque<>();
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    @Override
    public V take() {

        lock.lock();
        try {

            while (queue.isEmpty()) {
                notEmpty.await();
            }

            var res = queue.removeFirst();
            notFull.signal();

            return res;
        } catch (InterruptedException ex) {
            throw new IllegalStateException(ex);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(V value) {
        lock.lock();

        try {
            while (queue.size() >= maxQueueSize) {
                notFull.await();
            }
            queue.addLast(value);

            notEmpty.signal();
        } catch (InterruptedException ex) {
            throw new IllegalStateException(ex);
        } finally {
            lock.unlock();
        }
    }
}
