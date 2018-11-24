package com.timmattison.greengrass.cdd.communication;

import com.amazonaws.greengrass.javasdk.model.GGIotDataException;
import com.amazonaws.greengrass.javasdk.model.GGLambdaException;
import com.google.common.eventbus.Subscribe;
import com.timmattison.greengrass.cdd.events.PublishBinaryEvent;
import com.timmattison.greengrass.cdd.events.PublishMessageEvent;
import com.timmattison.greengrass.cdd.events.PublishObjectEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public interface Communication {
    Pattern builderPattern = Pattern.compile("([^.$]+)\\$\\1Builder$");

    @Subscribe
    default void catchall(Object object) {
        failOnBuilder(object);
    }

    @Subscribe
    default void publishObjectEvent(PublishObjectEvent publishObjectEvent) {
        try {
            publish(publishObjectEvent.getTopic(), publishObjectEvent.getObject());
        } catch (Exception e) {
            // Do not throw exceptions in event bus subscriber methods, but publish the exception so debugging is easier
            publishMessageEvent(PublishMessageEvent.builder().topic(publishObjectEvent.getTopic()).message("Publish or serialization of object failed [" + e.getMessage() + "]").build());
        }
    }

    @Subscribe
    default void publishMessageEvent(PublishMessageEvent publishMessageEvent) {
        try {
            Map map = new HashMap<>();
            map.put("message", publishMessageEvent.getMessage());
            publish(publishMessageEvent.getTopic(), map);
        } catch (Exception e) {
            // Do not throw exceptions in event bus subscriber methods
        }
    }

    @Subscribe
    default void publishBinaryEvent(PublishBinaryEvent publishBinaryEvent) {
        try {
            publish(publishBinaryEvent.getTopic(), publishBinaryEvent.getBytes());
        } catch (Exception e) {
            // Do not throw exceptions in event bus subscriber methods
        }
    }

    void publish(String topic, Object message) throws GGIotDataException, GGLambdaException;

    void publish(String topic, byte[] bytes) throws GGIotDataException, GGLambdaException;

    default void failOnBuilder(Object object) {
        if (isBuilder(object)) {
            publishMessageEvent(PublishMessageEvent.builder().topic("debug").message("Attempted to publish a builder [" + object.getClass().getTypeName() + "]").build());
        }
    }

    default boolean isBuilder(Object object) {
        return builderPattern.matcher(object.getClass().getTypeName()).find();
    }
}
