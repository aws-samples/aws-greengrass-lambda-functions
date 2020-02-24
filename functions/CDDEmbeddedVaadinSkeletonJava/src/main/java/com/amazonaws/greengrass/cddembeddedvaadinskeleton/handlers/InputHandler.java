package com.amazonaws.greengrass.cddembeddedvaadinskeleton.handlers;

import com.amazonaws.greengrass.cddembeddedvaadinskeleton.data.Topics;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.events.ImmutableMessageFromCloudEvent;
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
        return topic.equals(topics.getInputTopic());
    }

    @Override
    public void execute(ImmutableGreengrassLambdaEvent immutableGreengrassLambdaEvent) {
        String topic = immutableGreengrassLambdaEvent.getTopic().get();
        String message = new Gson().toJson(immutableGreengrassLambdaEvent.getInput());
        dispatcher.dispatch(ImmutableMessageFromCloudEvent.builder().topic(topic).message(message).build());
    }
}
