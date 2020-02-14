package com.amazonaws.greengrass.cdddocker.handlers;

import com.amazonaws.greengrass.cdddocker.data.DockerStopRequest;
import com.amazonaws.greengrass.cdddocker.data.ImmutableDockerStopRequest;
import com.amazonaws.greengrass.cdddocker.data.Topics;
import com.amazonaws.greengrass.cdddocker.docker.DockerHelper;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;

import javax.inject.Inject;
import java.util.Optional;

public class DockerStopRequestHandler {
    @Inject
    Topics topics;
    @Inject
    DockerHelper dockerHelper;
    @Inject
    Dispatcher dispatcher;

    @Inject
    public DockerStopRequestHandler() {
    }

    @Inject
    public void afterInject() {
        dispatcher.add(ImmutableDockerStopRequest.class, this::dockerStopRequest);
    }

    public void dockerStopRequest(DockerStopRequest dockerStopRequest) {
        try {
            Optional<String> optionalContainerId = dockerHelper.stopContainer(dockerStopRequest.getName());

            if (!optionalContainerId.isPresent()) {
                return;
            }

            dispatcher.publishMessageEvent(topics.getResponseTopic(), "Container stopped [" + dockerStopRequest.getName() + ", " + optionalContainerId.get() + "]");
        } catch (Exception e) {
            dispatcher.publishMessageEvent(topics.getResponseTopic(), e.getMessage());
        }
    }
}
