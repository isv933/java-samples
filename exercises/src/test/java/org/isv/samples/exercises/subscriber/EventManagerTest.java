package org.isv.samples.exercises.subscriber;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.function.Consumer;

public class EventManagerTest {


    @Test
    public void publishAndSubscribe() {
        var result = new ArrayList<Event>();


        Consumer<UserLogin> userLogin = (UserLogin event) -> {
            result.add(event);
        };

        var eventManager = new EventManager();
        eventManager.subscribe(UserLogin.class, userLogin);
        eventManager.publish(UserLogout.builder().name("2").build());
        eventManager.publish(UserLogin.builder().name("1").build());
        eventManager.unsubscribe(UserLogin.class, userLogin);
        eventManager.publish(UserLogin.builder().name("2").build());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(UserLogin.class, result.get(0).getClass());


    }

    @Data
    @Builder
    private static class UserLogin implements Event {
        private final String name;
    }

    @Data
    @Builder
    private static class UserLogout implements Event {
        private final String name;
        private final Instant logoutDate;
    }


}
