package org.isv.samples.exercises.threads;

import org.isv.samples.exercises.threads.DaemonThreadFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;

public class DaemonThreadFactoryTest {

    @Test
    public void testDaemonThread(){

        var executorService = Executors.newSingleThreadExecutor(new DaemonThreadFactory("daemon"));
        var task = executorService.submit(()->Thread.currentThread().isDaemon() && Thread.currentThread().getName().equals("daemon"));
        try {
            Assertions.assertTrue(task.get());

        }
        catch (Exception ex) {
            Assertions.fail(ex);
        }
        finally {
            executorService.shutdown();
        }
    }

}
