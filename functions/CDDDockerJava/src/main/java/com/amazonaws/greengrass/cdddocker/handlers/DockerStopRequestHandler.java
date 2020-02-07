package com.amazonaws.greengrass.cdddocker.handlers;

import com.amazonaws.greengrass.cdddocker.data.DockerStopRequest;
import com.amazonaws.greengrass.cdddocker.data.Topics;
import com.amazonaws.greengrass.cdddocker.docker.DockerHelper;
import com.awslabs.aws.iot.greengrass.cdd.communication.Communication;
import com.google.common.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.Optional;

public class DockerStopRequestHandler {
    @Inject
    Topics topics;
    @Inject
    DockerHelper dockerHelper;
    @Inject
    Communication communication;

    @Inject
    public DockerStopRequestHandler() {
    }

    @Subscribe
    public void dockerStopRequest(DockerStopRequest dockerStopRequest) {
        try {
            Optional<String> optionalContainerId = dockerHelper.stopContainer(dockerStopRequest.getName());

            if (!optionalContainerId.isPresent()) {
                return;
            }

            communication.publishMessageEvent(topics.getResponseTopic(), "Container stopped [" + dockerStopRequest.getName() + ", " + optionalContainerId.get() + "]");
        } catch (Exception e) {
            communication.publishMessageEvent(topics.getResponseTopic(), e.getMessage());
        }
    }
}
