package com.amazonaws.greengrass.cddskeleton.handlers;

import com.amazonaws.greengrass.cddskeleton.data.Topics;
import com.awslabs.aws.iot.greengrass.cdd.communication.Communication;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

public class StartupHandler implements GreengrassStartEventHandler {
    private static final int START_DELAY_MS = 5000;
    private static final int PERIOD_MS = 5000;
    @Inject
    Communication communication;
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
        communication.publishMessageEvent(topics.getOutputTopic(), "Skeleton started [" + System.currentTimeMillis() + "]");

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                communication.publishMessageEvent(topics.getOutputTopic(), "Skeleton still running... [" + System.currentTimeMillis() + "]");
            }
        }, START_DELAY_MS, PERIOD_MS);
    }
}
