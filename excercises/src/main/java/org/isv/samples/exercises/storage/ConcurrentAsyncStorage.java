package org.isv.samples.exercises.storage;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@RequiredArgsConstructor
public class ConcurrentAsyncStorage<K,V> implements AsyncStorage<K,V>, AutoCloseable {

    private final int maxCacheSize;
    private final int maxConcurrentStorageRequests;
    private final Duration maxShutdownTimeout;
    private final Function<K,V> storageSupplier;

    private final LRUCache cache = new LRUCache();
    private final Object  cacheLock = new Object();
    private final ExecutorService queueExecutor = Executors.newSingleThreadExecutor();
    private final ConcurrentHashMap<K, ArrayList<CompletableFuture<V>>> storageRequests = new ConcurrentHashMap<>();
    private final BlockingQueue<K> queue = new LinkedBlockingDeque<>();
    private final Future<?> queueTask = queueExecutor.submit(this::queueProcessor);

    @Override
    public CompletableFuture<V> get(K key) {
        synchronized (cacheLock) {
            if (cache.containsKey(key)) {
                return CompletableFuture.completedFuture(cache.get(key));
            }
        }

        return addStorageRequest(key);
    }


    private CompletableFuture<V> addStorageRequest(K key){
        var taskResult = new CompletableFuture<V>();

        var newTask = new AtomicBoolean();

        storageRequests.compute(key, (k,v)->{
            if (v == null) {
                newTask.set(true);
                return new ArrayList<>(List.of(taskResult));
            }
            v.add(taskResult);
            return v;
        });

        if (newTask.get()){
            queue.add(key);
        }

        return taskResult;
    }

    @Override
    public void close(){
        queueExecutor.shutdownNow();

        try {
                queueTask.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void queueProcessor() {
        var storageTasksExecutor = Executors.newFixedThreadPool(maxConcurrentStorageRequests);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                var taskRequest = queue.take();
                storageTasksExecutor.execute(() -> executeStorageTask(taskRequest));
            }
            catch(InterruptedException ex) {
                break;
            }
        }

        shutdownStorageRequestsTermination(storageTasksExecutor);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void shutdownStorageRequestsTermination(ExecutorService executor) {
        try {
            executor.shutdownNow();
            executor.awaitTermination(maxShutdownTimeout.toNanos(), TimeUnit.NANOSECONDS);
        }
        catch (InterruptedException ex) {
            //no nothing special when interrupted, because it is shutdown process
        }

        storageRequests.forEach((k,v)->
                notifyClients(k,new StorageResult<>(null, new InterruptedException("Storage was interrupted"))));
    }

    private void executeStorageTask(K key){
        var result = getStorageResult(key);
        notifyClients(key,result);
    }

    private StorageResult<V> getStorageResult(K key) {
        try {
            var result = new StorageResult<>(storageSupplier.apply(key), null);
            synchronized (cacheLock) {
                cache.put(key, result.result());
            }

            return result;
        }
        catch(Exception ex) {
            return new StorageResult<>(null, ex);
        }
    }

    private void notifyClients(K key, StorageResult<V> result) {
        var clients = storageRequests.remove(key);
        if (clients == null) {
            return;
        }

        for(var client: clients) {
            if (result.exception()!=null) {
                client.completeExceptionally(result.exception());
            } else {
                client.complete(result.result());
            }
        }
    }


    record StorageResult<V>(V result, Exception exception) {

    }

    class LRUCache extends LinkedHashMap<K, V> {

        LRUCache() {
            super(16, 0.75f, true);
        }
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size()>maxCacheSize;
        }
    }
}
