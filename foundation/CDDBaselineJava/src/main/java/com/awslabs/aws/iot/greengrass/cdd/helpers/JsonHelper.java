package com.awslabs.aws.iot.greengrass.cdd.helpers;

import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassLambdaEvent;

import java.util.Optional;

public interface JsonHelper {
    String toJson(Object object);

    <T> T fromJson(Class<T> clazz, byte[] json);

    <T> T fromJson(Class<T> clazz, String json);

    <T> Optional<T> fromJson(Class<T> clazz, GreengrassLambdaEvent greengrassLambdaEvent);
}
