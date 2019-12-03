package com.amazonaws.greengrass.cdddocker.handlers;

import com.amazonaws.greengrass.cdddocker.data.DockerStopRequest;
import com.amazonaws.greengrass.cdddocker.data.Topics;
import com.amazonaws.greengrass.cdddocker.docker.DockerHelper;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutablePublishMessageEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import javax.inject.Inject;
import java.util.Optional;

public class DockerStopRequestHandler {
    @Inject
    EventBus eventBus;
    @Inject
    Topics topics;
    @Inject
    DockerHelper dockerHelper;

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

            eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getResponseTopic()).message("Container stopped [" + dockerStopRequest.getName() + ", " + optionalContainerId.get() + "]").build());
        } catch (Exception e) {
            eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getResponseTopic()).message(e.getMessage()).build());
        }
    }
}
