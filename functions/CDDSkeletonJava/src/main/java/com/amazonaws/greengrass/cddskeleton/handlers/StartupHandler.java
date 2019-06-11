package com.amazonaws.greengrass.cddskeleton.handlers;

import com.amazonaws.greengrass.cddskeleton.data.Topics;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutablePublishMessageEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;
import com.google.common.eventbus.EventBus;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

public class StartupHandler implements GreengrassStartEventHandler {
    private static final int START_DELAY_MS = 5000;
    private static final int PERIOD_MS = 5000;
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
        eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getOutputTopic()).message("Skeleton started [" + System.currentTimeMillis() + "]").build());

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getOutputTopic()).message("Skeleton still running... [" + System.currentTimeMillis() + "]").build());
            }
        }, START_DELAY_MS, PERIOD_MS);
    }
}
