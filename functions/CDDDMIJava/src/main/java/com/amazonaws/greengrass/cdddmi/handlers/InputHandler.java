package com.amazonaws.greengrass.cdddmi.handlers;

import com.amazonaws.greengrass.cdddmi.data.Topics;
import com.amazonaws.greengrass.cdddmi.dmi.interfaces.DmiFetcher;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutablePublishObjectEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import com.google.common.eventbus.EventBus;

import javax.inject.Inject;

public class InputHandler implements GreengrassLambdaEventHandler {
    @Inject
    EventBus eventBus;
    @Inject
    DmiFetcher dmiFetcher;
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
    public void execute(GreengrassLambdaEvent greengrassLambdaEvent) {
        eventBus.post(ImmutablePublishObjectEvent.builder().topic(topics.getOutputTopic()).object(dmiFetcher.fetch().get()).build());
    }
}
