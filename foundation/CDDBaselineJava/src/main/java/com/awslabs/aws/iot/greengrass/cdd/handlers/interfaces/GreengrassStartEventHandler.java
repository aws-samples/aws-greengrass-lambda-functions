package com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces;

import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassStartEvent;

public interface GreengrassStartEventHandler {
    void execute(ImmutableGreengrassStartEvent immutableGreengrassStartEvent);
}
