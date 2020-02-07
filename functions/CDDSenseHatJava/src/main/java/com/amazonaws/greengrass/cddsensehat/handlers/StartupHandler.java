package com.amazonaws.greengrass.cddsensehat.handlers;

import com.amazonaws.greengrass.cddsensehat.data.Topics;
import com.amazonaws.greengrass.cddsensehat.events.TimerFiredEvent;
import com.awslabs.aws.iot.greengrass.cdd.communication.Communication;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassStartEvent;
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
    Communication communication;

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

        communication.publishMessageEvent(topics.getDebugOutputTopic(), "SenseHat started [" + System.currentTimeMillis() + "]");
        communication.publishMessageEvent(topics.getDebugOutputTopic(), "List topic: " + topics.getListTopic());
        communication.publishMessageEvent(topics.getDebugOutputTopic(), "Start topic: " + topics.getStartTopic());
        communication.publishMessageEvent(topics.getDebugOutputTopic(), "Stop topic: " + topics.getStopTopic());
    }
}
