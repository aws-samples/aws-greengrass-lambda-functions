package com.amazonaws.greengrass.cddembeddedvaadinskeleton.handlers;

import com.amazonaws.greengrass.cddembeddedvaadinskeleton.events.TimerFiredEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TimerFiredEventHandler {
    private final EventBus eventBus;

    @Subscribe
    public void timerFiredEvent(TimerFiredEvent timerFiredEvent) {
    }
}
