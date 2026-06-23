package org.isv.samples.exercises;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcurrentSingleFlightTest {
    @Mock
    private Function<String, Integer> loader;

    @Test
    public void shouldSingleCall() {
        var testKey = "myKey";
        var testResult = Integer.valueOf(((int) (Math.random() * 10)) % 10);
        var countDown = new CountDownLatch(1);

        when(loader.apply(testKey)).thenAnswer((arg) -> {
            try {
                countDown.await();
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);

            }
            return testResult;
        });

        try (var singleFlight = new ConcurrentSingleFlight<>(1, loader, Duration.ofMillis(1))) {
            var results = Stream.generate(() -> singleFlight.execute(testKey)).limit(5).toList();
            countDown.countDown();

            try {
                for (var result : results) {
                    Assertions.assertEquals(testResult, result.get());
                }
                verify(loader, only()).apply(testKey);
            } catch (Exception ex) {
                Assertions.fail(ex);
            }
        }
    }

    @Test
    public void shouldNotExecuteAfterClose() {
        var singleFlight = new ConcurrentSingleFlight<>(1, loader, Duration.ofMillis(1));
        var res = singleFlight.execute("test");
        try {
            Assertions.assertNull(res.get());
        } catch (Exception ex) {
            Assertions.fail(ex);
        }

        singleFlight.close();
        Assertions.assertThrows(IllegalStateException.class, () -> singleFlight.execute("test"));
    }


}