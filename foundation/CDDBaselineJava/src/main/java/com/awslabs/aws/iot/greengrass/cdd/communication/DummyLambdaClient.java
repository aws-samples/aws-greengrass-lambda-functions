package com.awslabs.aws.iot.greengrass.cdd.communication;

import com.amazonaws.greengrass.javasdk.model.InvokeRequest;
import com.amazonaws.greengrass.javasdk.model.InvokeResponse;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class DummyLambdaClient implements LambdaClientInterface {
    private final Logger log = LoggerFactory.getLogger(DummyLambdaClient.class);

    @Inject
    public DummyLambdaClient() {
    }

    @Override
    public InvokeResponse invoke(InvokeRequest invokeRequest) {
        log.info("Simulated Lambda invoke: " + new Gson().toJson(invokeRequest));

        return null;
    }
}
