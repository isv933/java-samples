package org.isv.samples.exercises;

import org.isv.samples.exercises.MemoryKeyValueStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class MemoryKeyValueStoreTest {
    @Test
    public void storeKeyValue() {
        try (var store = new MemoryKeyValueStore<String, Long>(Duration.ofSeconds(1), 1)) {
            store.put("key", 1L);
            var res = store.tryGet("key");
            Assertions.assertEquals(1L, res);
        }
    }

    @Test
    public void deleteKeyValue() {
        try (var store = new MemoryKeyValueStore<String, Long>(Duration.ofSeconds(1), 2)) {
            store.put("key", 1L);
            store.put("key1", 2L);
            var res = store.tryGet("key");
            Assertions.assertEquals(1L, res);
            store.delete("key");
            Assertions.assertNull(store.tryGet("key"));
            Assertions.assertEquals(2L, store.tryGet("key1"));
        }
    }

    @Test
    public void doNotGetExpiredKeys() {
        var ttl = Duration.ofMillis(100);
        try (var store = new MemoryKeyValueStore<String, Long>(ttl, 100)) {
            store.put("key", 1L);
            var res = store.tryGet("key");
            Assertions.assertEquals(1L, res);
            Thread.sleep(ttl.toMillis() + ttl.toMillis() / 2);
            store.tryGet("key");
            Assertions.assertNull(store.tryGet("key"));
        } catch (Exception ex) {
            Assertions.fail(ex);
        }
    }

    @Test
    public void doCleanupExpiredKeys() {
        var ttl = Duration.ofMillis(100);
        var maxCapacity = 100;
        var executorService = Executors.newCachedThreadPool();

        try (var store = new MemoryKeyValueStore<String, Long>(ttl, maxCapacity)) {
            for (var i = 0; i < maxCapacity; i++) {
                store.put("key" + i, 1L);
            }
            for (var i = 0; i < maxCapacity; i++) {
                var res = store.tryGet("key" + i);
                Assertions.assertEquals(1L, res);
            }

            CompletableFuture.allOf(
                    IntStream.range(0, maxCapacity)
                            .mapToObj(i -> CompletableFuture.runAsync(() -> store.put("key_" + i, 2L)))
                            .toArray(CompletableFuture[]::new)).join();

            for (var i = 0; i < maxCapacity; i++) {
                Assertions.assertEquals(2L, store.tryGet("key_" + i));
            }
        } catch (Exception ex) {
            Assertions.fail(ex);
        } finally {
            executorService.shutdown();
        }
    }


}
