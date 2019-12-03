package com.amazonaws.greengrass.cddlatencydashboard.events;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Latency {
    public abstract String getName();

    public abstract String getUnit();

    public abstract Double getValue();
}
