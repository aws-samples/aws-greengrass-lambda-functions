package com.awslabs.aws.iot.greengrass.cdd.communication;

import com.amazonaws.greengrass.javasdk.model.GGIotDataException;
import com.amazonaws.greengrass.javasdk.model.GGLambdaException;
import com.awslabs.aws.iot.greengrass.cdd.helpers.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DummyCommunication implements Communication {
    private final Logger log = LoggerFactory.getLogger(DummyCommunication.class);

    @Inject
    JsonHelper jsonHelper;

    @Inject
    public DummyCommunication() {
    }

    @Override
    public void publish(String topic, Object object) {
        log.info("Simulated MQTT message on topic [" + topic + "]: " + jsonHelper.toJson(object));
    }

    @Override
    public void publish(String topic, byte[] bytes) throws GGIotDataException, GGLambdaException {
        log.info("Simulated binary MQTT message on topic [" + topic + ": " + jsonHelper.toJson(bytes));
    }

    @Override
    public Optional<byte[]> invokeByName(String functionName, Optional<Map> customContext, byte[] binaryData) {
        log.info("Simulated invoke by name on function [" + functionName + ": " + jsonHelper.toJson(customContext.orElse(new HashMap())) + "[" + binaryData + "]");

        return Optional.empty();
    }
}
