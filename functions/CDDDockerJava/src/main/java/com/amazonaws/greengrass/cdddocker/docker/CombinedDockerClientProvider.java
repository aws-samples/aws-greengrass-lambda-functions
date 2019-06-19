package com.amazonaws.greengrass.cdddocker.docker;

import com.github.dockerjava.api.DockerClient;

import javax.inject.Inject;

public class CombinedDockerClientProvider {
    @Inject
    EcrDockerClientProvider ecrDockerClientProvider;
    @Inject
    NoAuthDockerClientProvider noAuthDockerClientProvider;

    public DockerClient getEcrDockerClient() {
        return ecrDockerClientProvider.get();
    }

    public DockerClient getLocalDockerClient() {
        return noAuthDockerClientProvider.get();
    }
}
