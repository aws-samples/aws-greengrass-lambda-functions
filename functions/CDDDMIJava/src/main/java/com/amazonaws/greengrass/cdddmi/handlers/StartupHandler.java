package com.amazonaws.greengrass.cdddmi.handlers;

import com.amazonaws.greengrass.cdddmi.data.Topics;
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
     * Receives the Greengrass start event from the event bus, publishes a message indicating it has started, and creates
     * a timer that publishes a message every 5 seconds after a 5 second delay
     *
     * @param greengrassStartEvent
     */
    @Override
    public void execute(GreengrassStartEvent greengrassStartEvent) {
        communication.publishMessageEvent(topics.getOutputTopic(), "DMI started [" + System.currentTimeMillis() + "]");
    }
}
