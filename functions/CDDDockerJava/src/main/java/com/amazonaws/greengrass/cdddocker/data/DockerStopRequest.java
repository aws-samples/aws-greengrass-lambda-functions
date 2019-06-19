package com.amazonaws.greengrass.cdddocker.data;

import org.immutables.value.Value;

@Value.Immutable
public abstract class DockerStopRequest {
    public abstract String getName();
}
