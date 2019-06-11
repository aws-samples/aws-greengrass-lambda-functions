package com.amazonaws.greengrass.cddsensehat.handlers;

import com.amazonaws.greengrass.cddsensehat.data.Topics;
import com.amazonaws.greengrass.cddsensehat.events.TimerFiredEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutablePublishMessageEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;
import com.google.common.eventbus.EventBus;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

public class StartupHandler implements GreengrassStartEventHandler {
    private final int DELAY_MS = 5000;
    private final int PERIOD_MS = 5000;
    @Inject
    EventBus eventBus;
    @Inject
    Topics topics;

    @Inject
    public StartupHandler() {
    }

    @Override
    public void execute(GreengrassStartEvent greengrassStartEvent) {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                eventBus.post(new TimerFiredEvent());
            }
        }, DELAY_MS, PERIOD_MS);

        eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getDebugOutputTopic()).message("SenseHat started [" + System.currentTimeMillis() + "]").build());
        eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getDebugOutputTopic()).message("List topic: " + topics.getListTopic()).build());
        eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getDebugOutputTopic()).message("Start topic: " + topics.getStartTopic()).build());
        eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getDebugOutputTopic()).message("Stop topic: " + topics.getStopTopic()).build());
    }
}
