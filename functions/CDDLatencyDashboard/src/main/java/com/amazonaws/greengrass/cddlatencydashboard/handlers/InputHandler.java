package com.amazonaws.greengrass.cddlatencydashboard.handlers;

import com.amazonaws.greengrass.cddlatencydashboard.data.Topics;
import com.amazonaws.greengrass.cddlatencydashboard.events.ImmutableMessageFromCloudEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;

import javax.inject.Inject;

public class InputHandler implements GreengrassLambdaEventHandler {
    @Inject
    EventBus eventBus;
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
    public void execute(GreengrassLambdaEvent greengrassLambdaEvent) {
        eventBus.post(ImmutableMessageFromCloudEvent.builder().topic(greengrassLambdaEvent.getTopic().get()).message(new Gson().toJson(greengrassLambdaEvent.getInput())).build());
    }
}
