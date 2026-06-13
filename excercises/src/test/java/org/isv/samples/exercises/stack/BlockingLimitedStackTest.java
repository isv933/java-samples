package org.isv.samples.exercises.stack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class BlockingLimitedStackTest {

    @Test
    public void pushPopShouldWork() {
        var stack = new BlockingLimitedStack<String>(1);
        var result = new ArrayList<String>();


        var popThread = CompletableFuture.runAsync(() ->
        {
            var res = stack.pop();
            result.add(res);
        });

        stack.push("test123");
        popThread.join();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("test123", result.get(0));
    }

    @Test
    public void limitShouldWork() {
        var maxSize = 1;
        var stack = new BlockingLimitedStack<String>(maxSize);
        var result = new ArrayList<String>();
        var barrier = new CountDownLatch(1);

        var pushes = IntStream.range(0, maxSize + 1).mapToObj(x -> CompletableFuture.runAsync(() -> {
            var val = "a" + x;
            stack.push(val);
            result.add(val);
            barrier.countDown();
        })).toList();

        try {
            barrier.await();
            Assertions.assertEquals(1, result.size());
            Assertions.assertEquals(result.get(0), stack.pop());
            pushes.forEach(CompletableFuture::join);
            Assertions.assertEquals(result.get(1), stack.pop());
        } catch (InterruptedException ex) {
            Assertions.fail(ex);
        }
    }

}
