package com.timmattison.greengrass.cdd.handlers.interfaces;

import com.google.common.eventbus.Subscribe;
import com.timmattison.greengrass.cdd.events.GreengrassStartEvent;

public interface GreengrassStartEventHandler {
    @Subscribe
    default void greengrassStartEvent(GreengrassStartEvent greengrassStartEvent) {
        execute(greengrassStartEvent);
    }

    void execute(GreengrassStartEvent greengrassStartEvent);
}
