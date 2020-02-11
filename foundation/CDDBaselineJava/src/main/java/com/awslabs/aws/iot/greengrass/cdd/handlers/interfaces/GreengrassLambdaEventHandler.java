package com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces;

import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassLambdaEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public interface GreengrassLambdaEventHandler {
    @Subscribe
    default void receiveMessage(GreengrassLambdaEvent greengrassLambdaEvent) {
        try {
            Optional<String> topic = greengrassLambdaEvent.getTopic();

            if (!topic.isPresent()) {
                // No topic?  This must be a direct invoke.
                executeInvoke(greengrassLambdaEvent);
                return;
            }

            if (!isTopicExpected(topic.get())) {
                // Not expected topic?  Skip it.
                return;
            }

            // Topic expected, normal message processing
            execute(greengrassLambdaEvent);
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
