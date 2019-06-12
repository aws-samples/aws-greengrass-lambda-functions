package com.amazonaws.greengrass.cddsensehat.events;

import org.immutables.value.Value;

@Value.Immutable
public abstract class MessageFromCloudEvent {
    public abstract String getTopic();

    public abstract String getMessage();
}
