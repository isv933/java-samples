package org.isv.samples.exercises;

import org.isv.samples.exercises.queue.BoundedBlockedQueueWithReentrantLock;
import org.isv.samples.exercises.queue.BoundedBlockedQueueWithSemaphore;
import org.isv.samples.exercises.queue.BoundedBlockedQueueWithWaitNotify;
import org.isv.samples.exercises.queue.Queue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class BoundedQueueTest {
    private static Stream<Queue<String>> singleBoundedQueue() {
        var maxQueueSize = 1;

        return Stream.of(new BoundedBlockedQueueWithWaitNotify<>(maxQueueSize),
                new BoundedBlockedQueueWithReentrantLock<>(maxQueueSize),
                new BoundedBlockedQueueWithSemaphore<>(maxQueueSize));
    }

    @ParameterizedTest
    @MethodSource("singleBoundedQueue")
    public void shouldWaitForPush(Queue<String> queue) {
        var start = new CountDownLatch(1);

        var popWorker = CompletableFuture.supplyAsync(() -> {
            start.countDown();
            return queue.take();
        });

        try {
            start.await();
            var testData = "xxx";
            queue.put(testData);
            Assertions.assertEquals(testData, popWorker.join());

        } catch (Exception ex) {
            Assertions.fail();

        }

    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @MethodSource("singleBoundedQueue")
    public void shouldWaitIfFull(Queue<String> queue) {

        queue.put("test123");
        //var pushWorker = CompletableFuture.runAsync(()-> queue.put("test1234"));


    }


}
