package com.amazonaws.greengrass.cdddmi.handlers;

import com.amazonaws.greengrass.cdddmi.data.Topics;
import com.amazonaws.greengrass.cdddmi.dmi.interfaces.DmiFetcher;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;

import javax.inject.Inject;

public class RequestHandler implements GreengrassLambdaEventHandler {
    @Inject
    DmiFetcher dmiFetcher;
    @Inject
    Topics topics;
    @Inject
    Dispatcher dispatcher;

    @Inject
    public RequestHandler() {
    }

    @Override
    public boolean isTopicExpected(String topic) {
        return topic.equals(topics.getInputTopic());
    }

    @Override
    public void execute(ImmutableGreengrassLambdaEvent immutableGreengrassLambdaEvent) {
        dmiFetcher.fetch().ifPresent(data -> dispatcher.publishObjectEvent(topics.getOutputTopic(), data));
    }
}
