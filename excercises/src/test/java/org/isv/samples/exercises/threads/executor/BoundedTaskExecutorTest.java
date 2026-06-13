package org.isv.samples.exercises.threads.executor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BoundedTaskExecutorTest {

    @Test
    public void submitTask(){
        try (var executor = new BoundedTaskExecutor(3,10)) {
            var result = executor.submitTask(() -> "test1");
            Assertions.assertEquals("test1", result.get());
        }
    }


}
