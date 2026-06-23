package org.isv.samples.exercises;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

public class ConcurrentSingleFlight<K, V> implements SingleFlight<K, V>, AutoCloseable {
    private final Function<K, V> loader;
    private final ConcurrentHashMap<K, CompletableFuture<V>> inFlightTasks = new ConcurrentHashMap<>();
    private final ExecutorService taskExecutor;
    private final Duration terminationTimeout;
    private final ReadWriteLock shutdownLock = new ReentrantReadWriteLock();
    private boolean closed;

    public ConcurrentSingleFlight(int maxConcurrentExecutes, Function<K, V> loader, Duration terminationTimeout) {
        if (maxConcurrentExecutes < 1) {
            throw new IllegalArgumentException("maxConcurrentExecutes should be larger than 0");
        }

        Objects.requireNonNull(loader, "Loader is not specified");

        if (terminationTimeout == null || terminationTimeout.isNegative() || terminationTimeout.isZero()) {
            throw new IllegalArgumentException("terminationTimeout should be positive");
        }

        this.loader = loader;
        this.terminationTimeout = terminationTimeout;
        this.taskExecutor = Executors.newFixedThreadPool(maxConcurrentExecutes);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void close() {
        shutdownLock.writeLock().lock();
        try {
            if (closed) {
                return;
            }

            taskExecutor.shutdownNow();
            taskExecutor.awaitTermination(terminationTimeout.toNanos(), TimeUnit.NANOSECONDS);

            for (var task : inFlightTasks.values()) {
                task.completeExceptionally(new InterruptedException("Task is canceled"));
            }

            inFlightTasks.clear();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        } finally {
            closed = true;
            shutdownLock.writeLock().unlock();
        }
    }

    @Override
    public CompletableFuture<V> execute(K key) {

        shutdownLock.readLock().lock();

        try {

            if (closed) {
                throw new IllegalStateException("Called in closed executor");
            }

            var shouldLoad = new AtomicBoolean();

            var result = inFlightTasks.computeIfAbsent(key, (k) -> {
                shouldLoad.set(true);
                return new CompletableFuture<>();
            });

            if (shouldLoad.get()) {

                try {
                    taskExecutor.execute(() -> runTask(key));
                } catch (Exception ex) {
                    completeTask(inFlightTasks.remove(key), ex, null);
                }
            }

            return result;
        } finally {
            shutdownLock.readLock().unlock();

        }
    }

    private void runTask(K key) {
        try {
            var result = loader.apply(key);
            completeTask(inFlightTasks.remove(key), null, result);
        } catch (Exception ex) {
            completeTask(inFlightTasks.remove(key), ex, null);
        }
    }

    private void completeTask(CompletableFuture<V> task, Exception exception, V result) {
        if (task == null) {
            return;
        }

        if (exception != null) {
            task.completeExceptionally(exception);

        } else {
            task.complete(result);
        }
    }
}
