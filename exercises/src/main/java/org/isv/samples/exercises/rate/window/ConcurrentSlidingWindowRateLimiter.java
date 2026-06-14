package org.isv.samples.exercises.rate.window;

import org.isv.samples.exercises.rate.RateLimiter;

import java.time.Duration;
import java.util.ArrayDeque;

public class ConcurrentSlidingWindowRateLimiter implements RateLimiter {

    private final ArrayDeque<Long> requests = new ArrayDeque<>();
    private final Object lock = new Object();
    private final int maxRequestsCount;
    private final Duration requestsWindow;


    public ConcurrentSlidingWindowRateLimiter(int maxRequestsCount, Duration requestsWindow) {

        if (requestsWindow == null || requestsWindow.isNegative() || requestsWindow.isZero()) {
            throw new IllegalArgumentException("requestsWindow should be positive");
        }

        if (maxRequestsCount < 1) {
            throw new IllegalArgumentException("maxRequestsCount should be greater than 0");
        }

        this.maxRequestsCount = maxRequestsCount;
        this.requestsWindow = requestsWindow;
    }

    @Override
    public boolean tryAcquire() {
        synchronized (lock) {
            var currentTime = System.nanoTime();

            removeObsoleteRequests(currentTime);
            if (requests.size() >= maxRequestsCount) {
                return false;
            }
            requests.addLast(currentTime);
        }
        return true;
    }

    private void removeObsoleteRequests(long currentTime) {
        while (!requests.isEmpty() && isRequestExpired(currentTime, requests.peekFirst())) {
            requests.removeFirst();
        }
    }

    private boolean isRequestExpired(long currentTime, long requestTime) {
        return currentTime - requestTime > requestsWindow.toNanos();
    }


}
