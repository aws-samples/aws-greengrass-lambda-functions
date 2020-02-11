package com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces;

import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassStartEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public interface GreengrassStartEventHandler {
    @Subscribe
    default void greengrassStartEvent(GreengrassStartEvent greengrassStartEvent) {
        execute(greengrassStartEvent);
    }

    void execute(GreengrassStartEvent greengrassStartEvent);
}
