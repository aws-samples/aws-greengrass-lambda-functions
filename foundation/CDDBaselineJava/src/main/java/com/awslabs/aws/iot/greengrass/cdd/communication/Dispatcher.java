package com.awslabs.aws.iot.greengrass.cdd.communication;

import java.util.function.Consumer;

public interface Dispatcher {
    <T> void add(Class<T> clazz, Consumer<T> consumer);

    <T> void dispatch(T message);

    void publishObjectEvent(String topic, Object object);

    void publishMessageEvent(String topic, String message);

    void publishBinaryEvent(String topic, byte[] bytes);
}

