package org.isv.samples.exercises.threads.executor.retry;

import org.isv.samples.exercises.threads.executor.retry.MaxCountRetryPolicy;
import org.isv.samples.exercises.threads.executor.retry.RetryExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class RetryExecutorTest {

    @Test
    public void shouldExecuteOneTime(){
        var maxThreads = 2;
        var executor = new RetryExecutor(maxThreads, Duration.ofSeconds(10));
        var tasks = Stream.generate(()->executor.executeAsync(()->1,
                new MaxCountRetryPolicy(MaxCountRetryPolicy
                            .Config.builder().maxRetryCount(2).retryDelay(Duration.ofSeconds(1)).build()))).
                limit(maxThreads * 2).toList();

        for(var task : tasks ){
            Assertions.assertEquals(1,getResult(task));
        }
    }

    <T> T getResult(CompletableFuture<T> result) {
        try {
            return result.get();
        }
        catch(InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }
}
