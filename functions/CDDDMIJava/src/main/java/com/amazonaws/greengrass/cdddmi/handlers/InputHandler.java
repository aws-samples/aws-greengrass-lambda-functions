package com.amazonaws.greengrass.cdddmi.handlers;

import com.amazonaws.greengrass.cdddmi.data.Topics;
import com.amazonaws.greengrass.cdddmi.dmi.interfaces.DmiFetcher;
import com.awslabs.aws.iot.greengrass.cdd.communication.Communication;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;

import javax.inject.Inject;

public class InputHandler implements GreengrassLambdaEventHandler {
    @Inject
    DmiFetcher dmiFetcher;
    @Inject
    Topics topics;
    @Inject
    Communication communication;

    @Inject
    public InputHandler() {
    }

    @Override
    public boolean isTopicExpected(String topic) {
        return topic.equals(topics.getInputTopic());
    }

    @Override
    public void execute(GreengrassLambdaEvent greengrassLambdaEvent) {
        dmiFetcher.fetch().ifPresent(data -> communication.publishObjectEvent(topics.getOutputTopic(), data));
    }
}
