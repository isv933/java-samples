package org.isv.samples.exercises.threads.executor.result;

public interface TaskResult<T> {
    void join();

    T get();
}
