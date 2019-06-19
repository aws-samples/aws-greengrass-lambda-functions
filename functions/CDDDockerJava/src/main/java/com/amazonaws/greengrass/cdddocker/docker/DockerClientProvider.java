package com.amazonaws.greengrass.cdddocker.docker;

import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;

import java.util.Optional;

public interface DockerClientProvider {
    String DOCKER_HOST = "DOCKER_HOST";

    default DockerClient get() {
        DefaultDockerClientConfig.Builder dockerClientConfigBuilder = DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .withDockerConfig(null);

        getUser().ifPresent(dockerClientConfigBuilder::withRegistryUsername);
        getPassword().ifPresent(dockerClientConfigBuilder::withRegistryPassword);
        getUrl().ifPresent(dockerClientConfigBuilder::withRegistryUrl);
        getDockerHost().ifPresent(dockerClientConfigBuilder::withDockerHost);

        return DockerClientBuilder
                .getInstance(dockerClientConfigBuilder.build())
                .build();
    }

    Optional<String> getUrl();

    Optional<String> getPassword();

    Optional<String> getUser();

    default Optional<String> getDockerHost() {
        // This could be "tcp://127.0.0.1:2375" in function.conf if Docker should be queried via TCP
        return getEnvironmentProvider().get(DOCKER_HOST);
    }

    EnvironmentProvider getEnvironmentProvider();
}
