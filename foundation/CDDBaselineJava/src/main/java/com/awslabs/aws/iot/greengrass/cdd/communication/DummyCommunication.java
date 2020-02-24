package com.awslabs.aws.iot.greengrass.cdd.communication;

import com.amazonaws.greengrass.javasdk.model.GGIotDataException;
import com.amazonaws.greengrass.javasdk.model.GGLambdaException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DummyCommunication implements Communication {
    private final Logger log = LoggerFactory.getLogger(DummyCommunication.class);

    @Inject
    public DummyCommunication() {
    }

    @Override
    public void publish(String topic, Object object) {
        log.info("Simulated MQTT message on topic [" + topic + "]: " + new Gson().toJson(object));
    }

    @Override
    public void publish(String topic, byte[] bytes) throws GGIotDataException, GGLambdaException {
        log.info("Simulated binary MQTT message on topic [" + topic + ": " + new Gson().toJson(bytes));
    }

    @Override
    public Optional<byte[]> invokeByName(String functionName, Optional<Map> customContext, byte[] binaryData) {
        log.info("Simulated invoke by name on function [" + functionName + ": " + new Gson().toJson(customContext.orElse(new HashMap())) + "[" + binaryData + "]");

        return Optional.empty();
    }
}
