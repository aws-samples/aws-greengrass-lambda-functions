package com.amazonaws.greengrass.cddembeddedvaadinskeleton.handlers;

import com.amazonaws.greengrass.cddembeddedvaadinskeleton.events.TimerFiredEvent;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;
import com.awslabs.aws.iot.greengrass.cdd.helpers.TimerHelper;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class StartupHandler implements GreengrassStartEventHandler {
    private static final int START_DELAY_MS = 5000;
    private static final int PERIOD_MS = 5000;

    @Inject
    Dispatcher dispatcher;
    @Inject
    TimerHelper timerHelper;

    @Inject
    public StartupHandler() {
    }

    @Override
    public void execute(ImmutableGreengrassStartEvent immutableGreengrassStartEvent) {
        Runnable runnable = () -> dispatcher.dispatch(new TimerFiredEvent());
        timerHelper.scheduleRunnable(runnable, START_DELAY_MS, PERIOD_MS, TimeUnit.MILLISECONDS);
    }
}
