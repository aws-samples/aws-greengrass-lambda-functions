package com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces;

import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassStartEvent;
import org.greenrobot.eventbus.Subscribe;

public interface GreengrassStartEventHandler {
    @Subscribe
    default void greengrassStartEvent(GreengrassStartEvent greengrassStartEvent) {
        execute(greengrassStartEvent);
    }

    void execute(GreengrassStartEvent greengrassStartEvent);
}
