package com.amazonaws.greengrass.cdddocker.handlers;

import com.amazonaws.greengrass.cdddocker.data.Topics;
import com.awslabs.aws.iot.greengrass.cdd.communication.Communication;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;

import javax.inject.Inject;

public class StartupHandler implements GreengrassStartEventHandler {
    @Inject
    Topics topics;
    @Inject
    Communication communication;

    @Inject
    public StartupHandler() {
    }

    /**
     * Receives the Greengrass start event from the event bus, and publishes a message indicating it has started
     *
     * @param greengrassStartEvent
     */
    @Override
    public void execute(GreengrassStartEvent greengrassStartEvent) {
        communication.publishMessageEvent(topics.getResponseTopic(), "Docker agent started [" + System.currentTimeMillis() + "]");
    }
}
