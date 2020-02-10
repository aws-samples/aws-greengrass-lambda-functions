package com.awslabs.aws.iot.greengrass.cdd.communication;

import com.amazonaws.greengrass.javasdk.model.GGIotDataException;
import com.amazonaws.greengrass.javasdk.model.GGLambdaException;
import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class DummyCommunication implements Communication {
    private final Logger log = LoggerFactory.getLogger(DummyCommunication.class);

    @Inject
    EventBus eventBus;

    public DummyCommunication(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public EventBus getEventBus() {
        return getEventBus();
    }

    @Override
    public void publish(String topic, Object object) {
        log.info("Simulated MQTT message on topic [" + topic + "]: " + new Gson().toJson(object));
    }

    @Override
    public void publish(String topic, byte[] bytes) throws GGIotDataException, GGLambdaException {
        log.info("Simulated binary MQTT message on topic [" + topic + ": " + new Gson().toJson(bytes));
    }
}
