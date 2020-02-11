package com.awslabs.aws.iot.greengrass.cdd.communication;

import com.awslabs.aws.iot.greengrass.cdd.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Dispatcher {
    private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);
    private static final Pattern builderPattern = Pattern.compile("([^.$]+)\\$\\1Builder$");
    private static final Map<Class, Set<Consumer>> dispatchTable = new HashMap<>();

    @Inject
    Communication communication;

    @Inject
    public Dispatcher() {
    }

    @Inject
    public void afterInject() {
        add(ImmutablePublishBinaryEvent.class, this::publishBinaryEvent);
        add(ImmutablePublishMessageEvent.class, this::publishMessageEvent);
        add(ImmutablePublishObjectEvent.class, this::publishObjectEvent);
    }

    public <T> void add(Class<T> clazz, Consumer<T> consumer) {
        synchronized (Dispatcher.class) {
            log.info("Adding consumer [" + consumer + "] for class [" + clazz + "]");
            dispatchTable.computeIfAbsent(clazz, key -> new HashSet<>()).add(consumer);

            log.info("-------------------------------------------------------------");
            log.info("Table after...");
            dispatchTable.forEach((key, value) -> log.info("Key [" + key + "] [" + value.stream().map(Consumer::toString).collect(Collectors.joining(" "))));
            log.info("-------------------------------------------------------------");
        }
    }

    public <T> void dispatch(T message) {
        Class clazz = message.getClass();
        Optional<Set<Consumer>> optionalConsumerSet = Optional.ofNullable(dispatchTable.get(clazz));

        if (!optionalConsumerSet.isPresent()) {
            log.warn("No consumers for [" + clazz + "]");
            catchall(message);
            return;
        }

        log.warn("Found consumers for [" + clazz + "]");
        Set<Consumer> consumerSet = optionalConsumerSet.get();

        log.info("Starting try consume loop...");
        log.info("Number of consumers: " + consumerSet.size());
        consumerSet.forEach(consumer -> tryConsume(consumer, message));
    }

    private <T> void tryConsume(Consumer<T> consumer, T message) {
        try {
            log.info("Calling consumer [" + consumer + "] on [" + message + "]");
            consumer.accept(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void catchall(Object object) {
        failOnBuilder(object);
    }

    private void publishObjectEvent(PublishObjectEvent publishObjectEvent) {
        try {
            communication.publish(publishObjectEvent.getTopic(), publishObjectEvent.getObject());
        } catch (Exception e) {
            // Do not throw exceptions in event bus subscriber methods, but publish the exception so debugging is easier
            publishMessageEvent(ImmutablePublishMessageEvent.builder().topic(publishObjectEvent.getTopic()).message("Publish or serialization of object failed [" + e.getMessage() + "]").build());
        }
    }

    private void publishMessageEvent(PublishMessageEvent publishMessageEvent) {
        try {
            Map map = new HashMap<>();
            map.put("message", publishMessageEvent.getMessage());
            communication.publish(publishMessageEvent.getTopic(), map);
        } catch (Exception e) {
            // Do not throw exceptions in event bus subscriber methods
        }
    }

    private void publishBinaryEvent(PublishBinaryEvent publishBinaryEvent) {
        try {
            communication.publish(publishBinaryEvent.getTopic(), publishBinaryEvent.getBytes());
        } catch (Exception e) {
            // Do not throw exceptions in event bus subscriber methods
        }
    }

    public void publishObjectEvent(String topic, Object object) {
        dispatch(ImmutablePublishObjectEvent.builder().topic(topic).object(object).build());
    }

    public void publishMessageEvent(String topic, String message) {
        dispatch(ImmutablePublishMessageEvent.builder().topic(topic).message(message).build());
    }

    public void publishBinaryEvent(String topic, byte[] bytes) {
        dispatch(ImmutablePublishBinaryEvent.builder().topic(topic).bytes(bytes).build());
    }

    private void failOnBuilder(Object object) {
        if (isBuilder(object)) {
            publishMessageEvent(ImmutablePublishMessageEvent.builder().topic("debug").message("Attempted to publish a builder [" + object.getClass().getTypeName() + "]").build());
        }
    }

    private boolean isBuilder(Object object) {
        return builderPattern.matcher(object.getClass().getTypeName()).find();
    }
}

