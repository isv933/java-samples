package org.isv.samples.exercises.threads;

import org.isv.samples.exercises.threads.Promise;
import org.isv.samples.exercises.threads.PromiseCountDown;
import org.isv.samples.exercises.threads.PromiseWait;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class PromiseTest {
    static Stream<Promise<String>> promiseProvider() {

        return Stream.of(new PromiseCountDown<String>(),new PromiseWait<>());

    }
    @ParameterizedTest
    @MethodSource("promiseProvider")
    public void shouldWaitResult(Promise<String> promise) {
        var testData = "test123";
        var getResult = CompletableFuture.supplyAsync(promise::get);
        promise.complete(testData);
        try {
            Assertions.assertEquals(testData, getResult.get());
            Assertions.assertEquals(testData, getResult.get());
        }
        catch (InterruptedException | ExecutionException ex) {
           Assertions.fail();
        }
    }

    @ParameterizedTest
    @MethodSource("promiseProvider")
    public void shouldExceptionResult(Promise<String> promise) {
        var getResult = CompletableFuture.supplyAsync(promise::get);
        promise.competeException(new InterruptedException());
        Assertions.assertThrows(IllegalStateException.class, ()->getExceptionResult(getResult));
        Assertions.assertThrows(IllegalStateException.class, ()->getExceptionResult(getResult));
    }

    private void getExceptionResult(CompletableFuture<String> action){
        try {
            action.get();
        }
        catch (InterruptedException | ExecutionException ex) {
            if (ex.getCause().getClass().equals(IllegalStateException.class)) {
                throw (IllegalStateException) ex.getCause();
            }
            throw new RuntimeException(ex);
        }
    }


}
