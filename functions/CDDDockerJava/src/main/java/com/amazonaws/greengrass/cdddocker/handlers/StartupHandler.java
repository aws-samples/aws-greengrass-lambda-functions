package com.amazonaws.greengrass.cdddocker.handlers;

import com.amazonaws.greengrass.cdddocker.data.Topics;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutablePublishMessageEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;
import com.google.common.eventbus.EventBus;

import javax.inject.Inject;

public class StartupHandler implements GreengrassStartEventHandler {
    @Inject
    EventBus eventBus;
    @Inject
    Topics topics;

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
        eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getResponseTopic()).message("Docker agent started [" + System.currentTimeMillis() + "]").build());
    }
}
