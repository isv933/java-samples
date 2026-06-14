package org.isv.samples.exercises.subscriber;

import java.util.function.Consumer;

public interface EventSubscriber {
    <T extends Event> void subscribe(Class<T> clazz, Consumer<T> callBack);

    <T extends Event> void unsubscribe(Class<T> clazz, Consumer<T> callBack);

}
