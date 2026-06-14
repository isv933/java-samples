package org.isv.samples.exercises.subscriber;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventManager implements EventPublisher, EventSubscriber {

    private final ConcurrentHashMap<Class<? extends Event>,
            List<Consumer<Event>>> subscribers = new ConcurrentHashMap<>();

    @Override
    public void publish(Event event) {

        var callbacks = subscribers.get(event.getClass());

        if (callbacks != null) {
            for (var callback : callbacks) {
                callback.accept(event);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> void subscribe(Class<T> clazz, Consumer<T> callBack) {

        var callbacks = subscribers.computeIfAbsent(clazz,
                (key) -> new CopyOnWriteArrayList<>());

        callbacks.add((Consumer<Event>) callBack);
    }

    @Override
    public <T extends Event> void unsubscribe(Class<T> clazz, Consumer<T> callBack) {
        var callbacks = subscribers.get(clazz);

        if (callbacks != null) {
            callbacks.remove(callBack);
        }
    }

}
