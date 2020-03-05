package com.amazonaws.greengrass.cddlatencydashboard.handlers;

import com.amazonaws.greengrass.cddlatencydashboard.data.Topics;
import com.amazonaws.greengrass.cddlatencydashboard.events.ImmutableMessageFromCloudEvent;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import com.google.gson.Gson;

import javax.inject.Inject;

public class InputHandler implements GreengrassLambdaEventHandler {
    @Inject
    Dispatcher dispatcher;
    @Inject
    Topics topics;

    @Inject
    public InputHandler() {
    }

    @Override
    public boolean isTopicExpected(String topic) {
        // Allow any topic
        return true;
    }

    @Override
    public void execute(ImmutableGreengrassLambdaEvent immutableGreengrassLambdaEvent) {
        String topic = immutableGreengrassLambdaEvent.getTopic().get();
        String message = new Gson().toJson(immutableGreengrassLambdaEvent.getInput());
        dispatcher.dispatch(ImmutableMessageFromCloudEvent.builder().topic(topic).message(message).build());
    }
}
