package com.amazonaws.greengrass.cddlatencydashboard.handlers;

import com.amazonaws.greengrass.cddlatencydashboard.events.TimerFiredEvent;
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
    }
}
