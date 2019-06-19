package com.amazonaws.greengrass.cdddocker.handlers;

import com.amazonaws.greengrass.cdddocker.data.*;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import com.google.common.eventbus.EventBus;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

public class RequestHandler implements GreengrassLambdaEventHandler {
    private static final String TYPE_KEY = "type";
    private static final String NAME_KEY = "name";
    @Inject
    EventBus eventBus;
    @Inject
    Topics topics;
    @Inject
    LoggingHelper loggingHelper;

    @Inject
    public RequestHandler() {
    }

    /**
     * Only care about messages on the topics.getRequestTopic() topic
     *
     * @param topic
     * @return
     */
    @Override
    public boolean isTopicExpected(String topic) {
        return topic.equals(topics.getRequestTopic());
    }

    /**
     * Send a message on the topics.getResponseTopic() topic showing that we received a message
     *
     * @param greengrassLambdaEvent
     */
    @Override
    public void execute(GreengrassLambdaEvent greengrassLambdaEvent) {
        LambdaLogger logger = greengrassLambdaEvent.getLogger();

        Optional<Map> optionalInput = greengrassLambdaEvent.getJsonInput();

        if (!optionalInput.isPresent()) {
            loggingHelper.logAndPublish(Optional.ofNullable(logger), topics.getResponseTopic(), "Empty message received on request topic");
            return;
        }

        Map input = optionalInput.get();

        if (!input.containsKey(TYPE_KEY)) {
            loggingHelper.logAndPublish(Optional.ofNullable(logger), topics.getResponseTopic(), "Message received on request topic without a type");
            return;
        }

        String typeString;

        try {
            typeString = (String) input.get(TYPE_KEY);
        } catch (ClassCastException e) {
            loggingHelper.logAndPublish(Optional.ofNullable(logger), topics.getResponseTopic(), "Message received on request topic with a type value that is not a string [" + e.getMessage() + "]");
            return;
        }

        DockerRequestType dockerRequestType;

        try {
            dockerRequestType = DockerRequestType.valueOf(typeString);
        } catch (IllegalArgumentException e) {
            loggingHelper.logAndPublish(Optional.ofNullable(logger), topics.getResponseTopic(), "Message received on request topic with a type value that was not understood [" + typeString + "]");
            return;
        }

        if (DockerRequestType.LIST == dockerRequestType) {
            // Just send the list request to the bus and let someone else handle it
            eventBus.post(ImmutableDockerListRequest.builder().build());
            return;
        }

        Optional<String> optionalName = Optional.ofNullable((String) input.get(NAME_KEY));

        if ((DockerRequestType.PULL == dockerRequestType) || (DockerRequestType.RUN == dockerRequestType) ||
                (DockerRequestType.STOP == dockerRequestType)) {
            if (!nameFieldPresent(logger, dockerRequestType, optionalName)) return;

            // Send the request to the bus with the name and let someone else handle it

            if (DockerRequestType.PULL == dockerRequestType) {
                DockerPullRequest dockerPullRequest = ImmutableDockerPullRequest.builder()
                        .name(optionalName.get())
                        .build();

                eventBus.post(dockerPullRequest);
                return;
            }

            if (DockerRequestType.RUN == dockerRequestType) {
                DockerRunRequest dockerRunRequest = ImmutableDockerRunRequest.builder()
                        .name(optionalName.get())
                        .build();

                eventBus.post(dockerRunRequest);
                return;
            }

            if (DockerRequestType.STOP == dockerRequestType) {
                DockerStopRequest dockerStopRequest = ImmutableDockerStopRequest.builder()
                        .name(optionalName.get())
                        .build();

                eventBus.post(dockerStopRequest);
                return;
            }
        }

        loggingHelper.logAndPublish(Optional.of(logger), topics.getResponseTopic(), "Not implemented yet [" + dockerRequestType + "]");
    }

    private boolean nameFieldPresent(LambdaLogger logger, DockerRequestType dockerRequestType, Optional<String> optionalName) {
        if (!optionalName.isPresent()) {
            loggingHelper.logAndPublish(Optional.ofNullable(logger), topics.getResponseTopic(), dockerRequestType.name() + " received with no name specified");
            return false;
        }

        return true;
    }
}
