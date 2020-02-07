package com.amazonaws.greengrass.cdddocker.handlers;

import com.amazonaws.greengrass.cdddocker.data.DockerRunRequest;
import com.amazonaws.greengrass.cdddocker.data.Topics;
import com.amazonaws.greengrass.cdddocker.docker.DockerHelper;
import com.awslabs.aws.iot.greengrass.cdd.communication.Communication;
import com.google.common.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.Optional;

public class DockerRunRequestHandler {
    @Inject
    Topics topics;
    @Inject
    DockerHelper dockerHelper;
    @Inject
    Communication communication;

    @Inject
    public DockerRunRequestHandler() {
    }

    @Subscribe
    public void dockerRunRequest(DockerRunRequest dockerRunRequest) {
        try {
            Optional<String> optionalContainerId = dockerHelper.createContainer(dockerRunRequest.getName());

            if (!optionalContainerId.isPresent()) {
                return;
            }

            communication.publishMessageEvent(topics.getResponseTopic(), "Container running [" + dockerRunRequest.getName() + ", " + optionalContainerId.get() + "]");
        } catch (Exception e) {
            communication.publishMessageEvent(topics.getResponseTopic(), e.getMessage());
        }
    }
}
