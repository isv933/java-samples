package org.isv.samples.exercises.threads.executor;

import lombok.Builder;
import lombok.Data;
import org.isv.samples.exercises.threads.executor.result.InternalTaskResult;
import org.isv.samples.exercises.threads.executor.result.TaskResult;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class BoundedTaskExecutor implements TaskExecutor {
    private final int maxThreadsNum;
    private final int maxTasksNum;
    private final ArrayBlockingQueue<Task<?>> tasksQueue;
    private final ConcurrentLinkedDeque<Thread> threads = new ConcurrentLinkedDeque<>();
    private final AtomicLong numTasks = new AtomicLong();
    private final Object threadsLock = new Object();
    private volatile boolean closed = false;

    public BoundedTaskExecutor(int maxThreadsNum, int maxTasksNum) {

        if (maxThreadsNum < 1) {
            throw new IllegalArgumentException("maxThreadsNum should be positive");
        }

        if (maxTasksNum < 1) {
            throw new IllegalArgumentException("maxTasksNum should be positive");
        }

        this.maxTasksNum = maxTasksNum;
        this.maxThreadsNum = maxThreadsNum;
        tasksQueue = new ArrayBlockingQueue<>(maxTasksNum);
    }


    @Override
    public <T> TaskResult<T> submitTask(Supplier<T> task) {
        Objects.requireNonNull(task);

        var queueTask = Task.<T>builder().supplyTask(task).result(new InternalTaskResult<>()).build();
        createNewTask(queueTask);

        return queueTask.getResult();
    }

    @Override
    public TaskResult<Void> submitTask(Runnable task) {
        Objects.requireNonNull(task);

        var queueTask = Task.<Void>builder().runTask(task).result(new InternalTaskResult<>()).build();
        createNewTask(queueTask);

        return queueTask.getResult();
    }

    @Override
    public void shutdown() {
        shutdownAll();
    }

    @Override
    public void close() {
        shutdownAll();
    }


    // shutdown is terminal;
    // executor is not expected to be used concurrently after shutdown starts;
    // holding lifecycle lock during shutdown is intentional

    private void shutdownAll() {
        synchronized (threadsLock) {
            closed = true;
            threads.forEach(Thread::interrupt);
            threads.forEach(this::joinThreadOnExit);
            tasksQueue.forEach(x -> x.getResult().setError(new RuntimeException("Task is canceled by shutdown process")));
            tasksQueue.clear();
            threads.clear();
        }
    }

    private void joinThreadOnExit(Thread thread) {
        var interrupted = false;

        while (true) {
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }

        if (interrupted) {
            Thread.currentThread().interrupt();

        }
    }

    private <T> void createNewTask(Task<T> task) {

        incrementTasks();
        createNewThreadIfRequired();
        tasksQueue.add(task);
    }

    private void createNewThreadIfRequired() {
        if (threads.size() < maxThreadsNum) {
            createNewThread();
        }
    }

    private void createNewThread() {
        synchronized (threadsLock) {
            if (isShouldWork() && threads.size() < maxThreadsNum) {

                var thread = new Thread(this::processTasksQueue);
                threads.addLast(thread);
                thread.start();
            }
        }
    }

    private void processTasksQueue() {
        while (isShouldWork()) {
            try {
                runTask(tasksQueue.take());
            } catch (InterruptedException ex) {
                //pass
            }
        }
    }

    private <T> void runTask(Task<T> task) {
        try {
            if (task.getSupplyTask() != null) {
                task.getResult().setResult(task.supplyTask.get());

            } else {
                task.runTask.run();
                task.getResult().setResult(null);
            }
        } catch (RuntimeException ex) {
            task.getResult().setError(ex);
        } finally {
            decrementTasks();

        }
    }


    private void incrementTasks() {
        while (true) {
            var currentTasks = numTasks.get();
            if (currentTasks == maxTasksNum) {
                throw new IllegalStateException("Cannot create new task, Too many tasks created");
            }

            if (numTasks.compareAndSet(currentTasks, currentTasks + 1)) {
                break;
            }
        }
    }

    private void decrementTasks() {
        while (true) {
            var currentTasks = numTasks.get();
            if (numTasks.compareAndSet(currentTasks, currentTasks - 1)) {
                break;
            }
        }

    }


    private boolean isShouldWork() {
        return !closed;
    }

    @Data
    @Builder
    static class Task<T> {
        private final Supplier<T> supplyTask;
        private final Runnable runTask;
        private final InternalTaskResult<T> result;
    }

}
