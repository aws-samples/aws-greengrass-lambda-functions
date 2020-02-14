package com.amazonaws.greengrass.cddsensehat.handlers;

import com.amazonaws.greengrass.cddsensehat.data.Topics;
import com.amazonaws.greengrass.cddsensehat.leds.animation.runner.interfaces.AnimationRunner;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class CddSenseHatStopEventHandler implements GreengrassLambdaEventHandler {
    private final Logger log = LoggerFactory.getLogger(CddSenseHatStopEventHandler.class);
    @Inject
    AnimationRunner animationRunner;
    @Inject
    Topics topics;

    @Inject
    public CddSenseHatStopEventHandler() {
    }

    @Override
    public boolean isTopicExpected(String topic) {
        return topic.equals(topics.getStopTopic());
    }

    @Override
    public void execute(ImmutableGreengrassLambdaEvent immutableGreengrassLambdaEvent) {
        log.info("Routing to stop...");
        animationRunner.stopAnimation();
    }
}
