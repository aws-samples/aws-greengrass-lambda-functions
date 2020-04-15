package com.amazonaws.greengrass.cddmdnsserviceresolver.data;

import io.vavr.collection.List;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.net.InetAddress;
import java.util.Optional;

@Gson.TypeAdapters
@Value.Immutable
public abstract class ServiceEvent {
    public abstract String getDomain();

    public abstract String getProtocol();

    public abstract String getApplication();

    public abstract String getName();

    public abstract Optional<List<InetAddress>> getInetAddresses();

    public abstract Optional<Integer> getPort();
}
