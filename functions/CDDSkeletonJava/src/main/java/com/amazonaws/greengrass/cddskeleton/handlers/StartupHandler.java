package com.amazonaws.greengrass.cddskeleton.handlers;

import com.amazonaws.greengrass.cddskeleton.data.Topics;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;
import com.awslabs.aws.iot.greengrass.cdd.helpers.TimerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class StartupHandler implements GreengrassStartEventHandler {
    private final Logger log = LoggerFactory.getLogger(StartupHandler.class);
    private static final int START_DELAY_MS = 5000;
    private static final int PERIOD_MS = 5000;
    @Inject
    Dispatcher dispatcher;
    @Inject
    Topics topics;
    @Inject
    TimerHelper timerHelper;

    @Inject
    public StartupHandler() {
    }

    /**
     * Receives the Greengrass start event from the event bus, publishes a message indicating it has started, and creates
     * a timer that publishes a message every 5 seconds after a 5 second delay
     *
     * @param immutableGreengrassStartEvent
     */
    @Override
    public void execute(ImmutableGreengrassStartEvent immutableGreengrassStartEvent) {
        dispatcher.publishMessageEvent(topics.getOutputTopic(), "Skeleton started [" + System.nanoTime() + "]");

        Runnable runnable = () -> dispatcher.publishMessageEvent(topics.getOutputTopic(), "Skeleton still running... [" + System.nanoTime() + "]");
        timerHelper.scheduleRunnable(runnable, START_DELAY_MS, PERIOD_MS, TimeUnit.MILLISECONDS);
    }
}
