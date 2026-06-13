package org.isv.samples.exercises.threads;

public interface Promise<T> {
    void complete(T result);
    void competeException(Throwable ex);
    T get();
}
