package com.amazonaws.greengrass.cdddocker.docker;

import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;

import javax.inject.Inject;
import java.util.Optional;

public class NoAuthDockerClientProvider implements DockerClientProvider {
    @Inject
    EnvironmentProvider environmentProvider;

    @Inject
    public NoAuthDockerClientProvider() {
    }

    @Override
    public Optional<String> getUrl() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getPassword() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getUser() {
        return Optional.empty();
    }

    @Override
    public EnvironmentProvider getEnvironmentProvider() {
        return environmentProvider;
    }
}
