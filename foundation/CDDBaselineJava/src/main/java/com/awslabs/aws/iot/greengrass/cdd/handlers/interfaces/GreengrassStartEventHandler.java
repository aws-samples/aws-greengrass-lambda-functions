package com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces;

import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassStartEvent;
import com.google.common.eventbus.Subscribe;

public interface GreengrassStartEventHandler {
    @Subscribe
    default void greengrassStartEvent(GreengrassStartEvent greengrassStartEvent) {
        execute(greengrassStartEvent);
    }

    void execute(GreengrassStartEvent greengrassStartEvent);
}
