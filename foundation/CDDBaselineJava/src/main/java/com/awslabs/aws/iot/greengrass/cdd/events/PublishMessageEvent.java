package com.awslabs.aws.iot.greengrass.cdd.events;

import org.immutables.value.Value;

@Value.Immutable
public abstract class PublishMessageEvent {
    public abstract String getTopic();

    public abstract String getMessage();
}
