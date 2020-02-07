package com.amazonaws.greengrass.cddkinesis.handlers;

import com.amazonaws.greengrass.cddkinesis.data.Topics;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutablePublishMessageEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import com.google.common.eventbus.EventBus;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class InputHandler implements GreengrassLambdaEventHandler {
    @Inject
    EventBus eventBus;
    @Inject
    Topics topics;

    @Inject
    public InputHandler() {
    }

    /**
     * Only care about messages on the topics.getInputTopic() topic
     *
     * @param topic
     * @return
     */
    @Override
    public boolean isTopicExpected(String topic) {
        return topic.equals(topics.getInputTopic());
    }

    /**
     * Send a message on the topics.getOutputTopic() topic showing that we received a message
     *
     * @param greengrassLambdaEvent
     */
    @Override
    public void execute(GreengrassLambdaEvent greengrassLambdaEvent) {
        String topic = greengrassLambdaEvent.getTopic().get();
        LambdaLogger logger = greengrassLambdaEvent.getLogger();

        String message = "Inbound message on topic [" + topic + "]";

        message += getInputDescription(greengrassLambdaEvent);

        logger.log(message);

        eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getOutputTopic()).message(message).build());
    }

    @Override
    public void executeInvoke(GreengrassLambdaEvent greengrassLambdaEvent) {
        LambdaLogger logger = greengrassLambdaEvent.getLogger();

        String message = "Function invoked directly as a Lambda";

        message += getInputDescription(greengrassLambdaEvent);

        logger.log(message);

        eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getOutputTopic()).message(message).build());
    }

    private String getInputDescription(GreengrassLambdaEvent greengrassLambdaEvent) {
        if (greengrassLambdaEvent.getBinaryInput().isPresent()) {
            return ", with BINARY input";
        }

        if (greengrassLambdaEvent.getJsonInput().isPresent()) {
            return ", with JSON input";
        }

        return ", WITHOUT input";
    }


}
