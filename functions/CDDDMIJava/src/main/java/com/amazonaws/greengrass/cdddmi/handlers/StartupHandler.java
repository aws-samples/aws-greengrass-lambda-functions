package com.amazonaws.greengrass.cdddmi.handlers;

import com.amazonaws.greengrass.cdddmi.data.Topics;
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
     * Receives the Greengrass start event from the event bus, publishes a message indicating it has started, and creates
     * a timer that publishes a message every 5 seconds after a 5 second delay
     *
     * @param greengrassStartEvent
     */
    @Override
    public void execute(GreengrassStartEvent greengrassStartEvent) {
        eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getOutputTopic()).message("DMI started [" + System.currentTimeMillis() + "]").build());
    }
}
