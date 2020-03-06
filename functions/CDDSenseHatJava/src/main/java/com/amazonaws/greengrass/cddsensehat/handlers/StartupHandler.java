package com.amazonaws.greengrass.cddsensehat.handlers;

import com.amazonaws.greengrass.cddsensehat.data.Topics;
import com.amazonaws.greengrass.cddsensehat.events.TimerFiredEvent;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

public class StartupHandler implements GreengrassStartEventHandler {
    private final int DELAY_MS = 5000;
    private final int PERIOD_MS = 5000;
    @Inject
    Topics topics;
    @Inject
    Dispatcher dispatcher;

    @Inject
    public StartupHandler() {
    }

    @Override
    public void execute(ImmutableGreengrassStartEvent immutableGreengrassStartEvent) {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                dispatcher.dispatch(new TimerFiredEvent());
            }
        }, DELAY_MS, PERIOD_MS);

        dispatcher.publishMessageEvent(topics.getDebugOutputTopic(), "SenseHat started [" + System.nanoTime() + "]");
        dispatcher.publishMessageEvent(topics.getDebugOutputTopic(), "List topic: " + topics.getListTopic());
        dispatcher.publishMessageEvent(topics.getDebugOutputTopic(), "Start topic: " + topics.getStartTopic());
        dispatcher.publishMessageEvent(topics.getDebugOutputTopic(), "Stop topic: " + topics.getStopTopic());
    }
}
