package org.isv.samples.exercises.threads.executor;

import org.isv.samples.exercises.threads.executor.result.TaskResult;

import java.util.function.Supplier;

public interface TaskExecutor extends AutoCloseable {
    <T> TaskResult<T> submitTask(Supplier<T> task);

    TaskResult<Void> submitTask(Runnable task);

    void shutdown();
}
