package org.isv.samples.exercises;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.IntStream;

public class LRUCacheSimpleTest {

    @Test
    public void concurrentReadWrite() {
        var numConcurrents = 10;
        var cache = new LRUCacheSimple<String, Integer>(numConcurrents + 1);
        var conditional = new CyclicBarrier(numConcurrents * 2 + 1);
        var executorService = Executors.newFixedThreadPool(numConcurrents * 2);
        Function<Integer, String> keyGen = ((i) -> "key" + i);


        var getters = IntStream.range(0, numConcurrents).mapToObj(i -> executorService.submit(() ->
                {
                    try {
                        conditional.await();
                        var myKey = keyGen.apply(i);
                        while (true) {
                            var res = cache.tryGet(myKey);
                            if (res != null && res == i) {
                                cache.put(myKey, res + 1);
                                break;
                            }
                        }
                    } catch (InterruptedException | BrokenBarrierException e) {
                        return;
                    }
                }
        )).toList();

        var putters = IntStream.range(0, numConcurrents).boxed()
                .sorted(Comparator.reverseOrder()).map(i -> executorService.submit(() -> {
                    try {
                        conditional.await();
                        var myKey = keyGen.apply(i);
                        cache.put(myKey, i);
                    } catch (InterruptedException | BrokenBarrierException e) {
                        return;
                    }
                })).toList();

        try {
            conditional.await();
            for (var getter : getters) {
                getter.get(1, java.util.concurrent.TimeUnit.SECONDS);
                getter.get();
            }

            for (var putter : putters) {
                putter.get(1, java.util.concurrent.TimeUnit.SECONDS);
                putter.get();
            }

            Assertions.assertTrue(IntStream.range(0, numConcurrents).boxed().allMatch(x ->
                    x + 1 == cache.tryGet(keyGen.apply((x)))));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            executorService.shutdownNow();
        }
    }

    @Test
    public void removeLastWhenLimitExceed() {
        var maxCacheSize = 3;
        var cache = new LRUCacheSimple<Integer, Integer>(maxCacheSize);
        IntStream.range(0, maxCacheSize + 1).boxed().forEach(x -> cache.put(x, x));
        Assertions.assertNull(cache.tryGet(0));
        IntStream.range(1, maxCacheSize + 1).boxed().forEach(x -> Assertions.assertEquals(x, cache.tryGet(x)));
    }
}
