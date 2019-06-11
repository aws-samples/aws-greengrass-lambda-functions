package com.timmattison.greengrass.cdd.events;

import org.immutables.value.Value;

@Value.Immutable
public abstract class PublishObjectEvent {
    public abstract String getTopic();

    public abstract Object getObject();
}
