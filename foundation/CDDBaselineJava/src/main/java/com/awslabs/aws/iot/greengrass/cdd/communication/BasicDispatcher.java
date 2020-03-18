package com.awslabs.aws.iot.greengrass.cdd.communication;

import com.awslabs.aws.iot.greengrass.cdd.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class BasicDispatcher implements Dispatcher {
    private static final Logger log = LoggerFactory.getLogger(BasicDispatcher.class);
    private static final Pattern builderPattern = Pattern.compile("([^.$]+)\\$\\1Builder$");
    private static final Map<Class, Set<Consumer>> dispatchTable = new HashMap<>();

    @Inject
    Communication communication;

    @Inject
    public BasicDispatcher() {
    }

    @Inject
    public void afterInject() {
        add(ImmutablePublishBinaryEvent.class, this::publishBinaryEvent);
        add(ImmutablePublishMessageEvent.class, this::publishMessageEvent);
        add(ImmutablePublishObjectEvent.class, this::publishObjectEvent);
    }

    @Override
    public <T> Consumer<T> add(Class<T> clazz, Consumer<T> consumer) {
        synchronized (BasicDispatcher.class) {
            dispatchTable.computeIfAbsent(clazz, key -> new HashSet<>()).add(consumer);
        }

        return consumer;
    }

    @Override
    public <T> void remove(Consumer<T> consumer) {
        synchronized (BasicDispatcher.class) {
            dispatchTable.forEach((key, value) -> value.remove(consumer));
        }
    }

    @Override
    public <T> void dispatch(T message) {
        Class clazz = message.getClass();
        Optional<Set<Consumer>> optionalConsumerSet = Optional.ofNullable(dispatchTable.get(clazz));

        if (!optionalConsumerSet.isPresent()) {
            log.warn("No consumers for [" + clazz + "]");

            if (!isBuilder(message)) {
                return;
            }

            // This is a builder, this may be a partial object that the user forgot to fully build
            String errorMessage = "Attempted to publish a builder [" + message.getClass().getTypeName() + "]";
            log.error(errorMessage);
            publishMessageEvent("debug", errorMessage);
        }

        log.debug("Found consumers for [" + clazz + "]");
        Set<Consumer> consumerSet = optionalConsumerSet.get();

        log.debug("Starting try consume loop...");
        log.debug("Number of consumers: " + consumerSet.size());
        consumerSet.forEach(consumer -> tryConsume(consumer, message));
    }

    private <T> void tryConsume(Consumer<T> consumer, T message) {
        try {
            log.debug("Calling consumer [" + consumer + "] on [" + message + "]");
            consumer.accept(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void publishObjectEvent(PublishObjectEvent publishObjectEvent) {
        log.debug("In publishObjectEvent [" + publishObjectEvent + "]");
        try {
            communication.publish(publishObjectEvent.getTopic(), publishObjectEvent.getObject());
        } catch (Exception e) {
            // Do not throw exceptions in event bus subscriber methods, but publish the exception so debugging is easier
            publishMessageEvent(ImmutablePublishMessageEvent.builder().topic(publishObjectEvent.getTopic()).message("Publish or serialization of object failed [" + e.getMessage() + "]").build());
        }
    }

    private void publishMessageEvent(PublishMessageEvent publishMessageEvent) {
        log.debug("In publishMessageEvent [" + publishMessageEvent + "]");
        try {
            Map map = new HashMap<>();
            map.put("message", publishMessageEvent.getMessage());
            communication.publish(publishMessageEvent.getTopic(), map);
        } catch (Exception e) {
            // Do not throw exceptions in event bus subscriber methods
        }
    }

    private void publishBinaryEvent(PublishBinaryEvent publishBinaryEvent) {
        log.debug("In publishBinaryEvent [" + publishBinaryEvent + "]");
        try {
            communication.publish(publishBinaryEvent.getTopic(), publishBinaryEvent.getBytes());
        } catch (Exception e) {
            // Do not throw exceptions in event bus subscriber methods
        }
    }

    @Override
    public void publishObjectEvent(String topic, Object object) {
        dispatch(ImmutablePublishObjectEvent.builder().topic(topic).object(object).build());
    }

    @Override
    public void publishMessageEvent(String topic, String message) {
        dispatch(ImmutablePublishMessageEvent.builder().topic(topic).message(message).build());
    }

    @Override
    public void publishBinaryEvent(String topic, byte[] bytes) {
        dispatch(ImmutablePublishBinaryEvent.builder().topic(topic).bytes(bytes).build());
    }

    private boolean isBuilder(Object object) {
        return builderPattern.matcher(object.getClass().getTypeName()).find();
    }
}

