package com.awslabs.aws.iot.greengrass.cdd.helpers;

public interface JsonHelper {
    String toJson(Object object);

    <T> T fromJson(Class<T> clazz, byte[] json);

    <T> T fromJson(Class<T> clazz, String json);
}
