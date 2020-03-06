package com.amazonaws.greengrass.cdddocker.handlers;

import com.amazonaws.greengrass.cdddocker.data.Topics;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;

import javax.inject.Inject;

public class StartupHandler implements GreengrassStartEventHandler {
    @Inject
    Topics topics;
    @Inject
    Dispatcher dispatcher;

    @Inject
    public StartupHandler() {
    }

    /**
     * Receives the Greengrass start event from the event bus, and publishes a message indicating it has started
     *
     * @param immutableGreengrassStartEvent
     */
    @Override
    public void execute(ImmutableGreengrassStartEvent immutableGreengrassStartEvent) {
        dispatcher.publishMessageEvent(topics.getResponseTopic(), "Docker agent started [" + System.nanoTime() + "]");
    }
}
