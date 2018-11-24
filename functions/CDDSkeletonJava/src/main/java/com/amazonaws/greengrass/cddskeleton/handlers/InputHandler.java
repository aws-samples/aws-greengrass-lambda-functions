package com.amazonaws.greengrass.cddskeleton.handlers;

import com.amazonaws.greengrass.cddskeleton.data.Topics;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.common.eventbus.EventBus;
import com.timmattison.greengrass.cdd.events.GreengrassLambdaEvent;
import com.timmattison.greengrass.cdd.events.PublishMessageEvent;
import com.timmattison.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InputHandler implements GreengrassLambdaEventHandler {
    private final EventBus eventBus;
    private final Topics topics;

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

        eventBus.post(PublishMessageEvent.builder().topic(topics.getOutputTopic()).message(message).build());
    }

    @Override
    public void executeInvoke(GreengrassLambdaEvent greengrassLambdaEvent) {
        LambdaLogger logger = greengrassLambdaEvent.getLogger();

        String message = "Function invoked directly as a Lambda";

        message += getInputDescription(greengrassLambdaEvent);

        logger.log(message);

        eventBus.post(PublishMessageEvent.builder().topic(topics.getOutputTopic()).message(message).build());
    }

    public String getInputDescription(GreengrassLambdaEvent greengrassLambdaEvent) {
        if (greengrassLambdaEvent.getBinaryInput().isPresent()) {
            return ", with BINARY input";
        }

        if (greengrassLambdaEvent.getJsonInput().isPresent()) {
            return ", with JSON input";
        }

        return ", WITHOUT input";
    }
}
