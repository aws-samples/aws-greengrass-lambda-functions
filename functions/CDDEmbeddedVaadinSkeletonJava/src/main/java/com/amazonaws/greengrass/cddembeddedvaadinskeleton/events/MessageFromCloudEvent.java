package com.amazonaws.greengrass.cddembeddedvaadinskeleton.events;

import org.immutables.value.Value;

@Value.Immutable
public abstract class MessageFromCloudEvent {
    public abstract String getTopic();

    public abstract String getMessage();
}
