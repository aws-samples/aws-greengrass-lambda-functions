package com.timmattison.greengrass.cdd.communication;

import com.amazonaws.greengrass.javasdk.model.GGIotDataException;
import com.amazonaws.greengrass.javasdk.model.GGLambdaException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DummyCommunication implements Communication {
    @Override
    public void publish(String topic, Object object) {
        log.info("Simulated MQTT message on topic [" + topic + "]: " + new Gson().toJson(object));
    }

    @Override
    public void publish(String topic, byte[] bytes) throws GGIotDataException, GGLambdaException {
        log.info("Simulated binary MQTT message on topic [" + topic + ": " + new Gson().toJson(bytes));
    }
}
