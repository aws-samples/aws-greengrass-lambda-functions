package com.amazonaws.greengrass.cdddocker.handlers;

import com.amazonaws.greengrass.cdddocker.data.DockerRunRequest;
import com.amazonaws.greengrass.cdddocker.data.ImmutableDockerRunRequest;
import com.amazonaws.greengrass.cdddocker.data.Topics;
import com.amazonaws.greengrass.cdddocker.docker.DockerHelper;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;

import javax.inject.Inject;
import java.util.Optional;

public class DockerRunRequestHandler {
    @Inject
    Topics topics;
    @Inject
    DockerHelper dockerHelper;
    @Inject
    Dispatcher dispatcher;

    @Inject
    public DockerRunRequestHandler() {
    }

    @Inject
    public void afterInject() {
        dispatcher.add(ImmutableDockerRunRequest.class, this::dockerRunRequest);
    }

    public void dockerRunRequest(DockerRunRequest dockerRunRequest) {
        try {
            Optional<String> optionalContainerId = dockerHelper.createContainer(dockerRunRequest.getName());

            if (!optionalContainerId.isPresent()) {
                return;
            }

            dispatcher.publishMessageEvent(topics.getResponseTopic(), "Container running [" + dockerRunRequest.getName() + ", " + optionalContainerId.get() + "]");
        } catch (Exception e) {
            dispatcher.publishMessageEvent(topics.getResponseTopic(), e.getMessage());
        }
    }
}
