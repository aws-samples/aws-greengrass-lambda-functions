package com.awslabs.aws.iot.greengrass.cdd.communication;

import com.amazonaws.greengrass.javasdk.model.GGIotDataException;
import com.amazonaws.greengrass.javasdk.model.GGLambdaException;

import java.util.Map;
import java.util.Optional;

public interface Communication {
    void publish(String topic, Object object) throws GGIotDataException, GGLambdaException;

    void publish(String topic, byte[] bytes) throws GGIotDataException, GGLambdaException;

    Optional<byte[]> invokeByName(String functionName, Optional<Map> customContext, byte[] binaryData);
}
