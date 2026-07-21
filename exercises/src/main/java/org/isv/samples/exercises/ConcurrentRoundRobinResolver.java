package org.isv.samples.exercises;

import lombok.RequiredArgsConstructor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@RequiredArgsConstructor
public class ConcurrentRoundRobinResolver {
    private final int numServers;
    private final Function<Integer, String> getServer;
    private final AtomicInteger serverNum = new AtomicInteger();

    public String resolveServer() {
        while (true) {
            var current = serverNum.get();
            var next = current < numServers - 1 ? current + 1 : 0;
            if (serverNum.compareAndSet(current, next)) {
                return getServer.apply(current);
            }
        }
    }
}
