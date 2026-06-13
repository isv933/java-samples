package org.isv.samples.exercises.queue;

public interface Queue<V> {
    V take();
    void put(V value);
}
