package com.amazonaws.greengrass.cddlatencydashboard.events;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Latencies {
    public abstract String getUuid();

    public abstract List<Latency> getLatencies();
}
