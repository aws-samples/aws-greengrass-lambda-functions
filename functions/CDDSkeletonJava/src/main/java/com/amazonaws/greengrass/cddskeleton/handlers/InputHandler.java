package com.amazonaws.greengrass.cddskeleton.handlers;

import com.amazonaws.greengrass.cddskeleton.data.Topics;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class InputHandler implements GreengrassLambdaEventHandler {
    private final Logger log = LoggerFactory.getLogger(InputHandler.class);
    @Inject
    Dispatcher dispatcher;
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
     * @param immutableGreengrassLambdaEvent
     */
    @Override
    public void execute(ImmutableGreengrassLambdaEvent immutableGreengrassLambdaEvent) {
        log.info("Received input event");
        String topic = immutableGreengrassLambdaEvent.getTopic().get();
        LambdaLogger logger = immutableGreengrassLambdaEvent.getLogger();

        String message = "Inbound message on topic [" + topic + "]";

        message += getInputDescription(immutableGreengrassLambdaEvent);

        logger.log(message);

        dispatcher.publishMessageEvent(topics.getOutputTopic(), message);
    }

    @Override
    public void executeInvoke(ImmutableGreengrassLambdaEvent immutableGreengrassLambdaEvent) {
        LambdaLogger logger = immutableGreengrassLambdaEvent.getLogger();

        String message = "Function invoked directly as a Lambda";

        message += getInputDescription(immutableGreengrassLambdaEvent);

        logger.log(message);

        dispatcher.publishMessageEvent(topics.getOutputTopic(), message);
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
