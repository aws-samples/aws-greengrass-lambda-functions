package com.amazonaws.greengrass.cddembeddedvaadinskeleton.handlers;

import com.amazonaws.greengrass.cddembeddedvaadinskeleton.events.TimerFiredEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import javax.inject.Inject;

public class TimerFiredEventHandler {
    @Inject
    EventBus eventBus;

    @Inject
    public TimerFiredEventHandler() {
    }

    @Subscribe
    public void timerFiredEvent(TimerFiredEvent timerFiredEvent) {
    }
}
