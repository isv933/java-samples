package org.isv.samples.exercises;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;


class ConcurrentRoundRobinResolverTest {

    @Test
    public void resoleServerConcurrent() {
        var numServers = 5;
        var resolver = new ConcurrentRoundRobinResolver(numServers, (num) -> num + ".com");
        var tasks = Stream.generate(() ->
                CompletableFuture.supplyAsync(resolver::resolveServer)).limit(numServers + 1).toList();
        var resultAddress = tasks.stream().map(CompletableFuture::join).sorted().toArray(String[]::new);

        Assertions.assertEquals(numServers + 1, resultAddress.length);
        Assertions.assertEquals("0.com", resultAddress[0]);
        Assertions.assertEquals(resultAddress[0], resultAddress[1]);
        for (var i = 1; i < resultAddress.length; i++) {
            Assertions.assertEquals(String.format("%d.com", i - 1), resultAddress[i]);
        }
    }

}