package com.amazonaws.greengrass.cddsensehat.handlers;

import com.amazonaws.greengrass.cddsensehat.data.Topics;
import com.amazonaws.greengrass.cddsensehat.leds.animation.runner.interfaces.AnimationRunner;
import com.timmattison.greengrass.cdd.events.GreengrassLambdaEvent;
import com.timmattison.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CddSenseHatStopEventHandler implements GreengrassLambdaEventHandler {
    private final AnimationRunner animationRunner;
    private final Topics topics;

    @Override
    public boolean isTopicExpected(String topic) {
        return topic.equals(topics.getStopTopic());
    }

    @Override
    public void execute(GreengrassLambdaEvent greengrassLambdaEvent) {
        log.info("Routing to stop...");
        animationRunner.stopAnimation();
    }
}
