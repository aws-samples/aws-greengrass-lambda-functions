package com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces;

import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassLambdaEvent;

import java.util.Optional;

public interface GreengrassLambdaEventHandler {
    default void receiveMessage(ImmutableGreengrassLambdaEvent immutableGreengrassLambdaEvent) {
        try {
            Optional<String> topic = immutableGreengrassLambdaEvent.getTopic();

            if (!topic.isPresent()) {
                // No topic?  This must be a direct invoke.
                executeInvoke(immutableGreengrassLambdaEvent);
                return;
            }

            if (!isTopicExpected(topic.get())) {
                // Not expected topic?  Skip it.
                return;
            }

            // Topic expected, normal message processing
            execute(immutableGreengrassLambdaEvent);
        } catch (Exception e) {
            // Do not throw exceptions in event bus subscriber methods
        }
    }

    boolean isTopicExpected(String topic);

    void execute(GreengrassLambdaEvent greengrassLambdaEvent);

    default void executeInvoke(GreengrassLambdaEvent greengrassLambdaEvent) {
        // Default executeInvoke handler for legacy functions.  Make sure that the topic is never empty.
        ImmutableGreengrassLambdaEvent immutableGreengrassLambdaEvent = ImmutableGreengrassLambdaEvent
                .copyOf(greengrassLambdaEvent)
                .withTopic(Optional.of(greengrassLambdaEvent.getTopic().orElse("NULL")));

        // Execute the normal message processing
        execute(greengrassLambdaEvent);
    }
}
