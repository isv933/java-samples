package org.isv.samples.exercises.rate;


public interface RateLimiter {
    boolean tryAcquire();
}
