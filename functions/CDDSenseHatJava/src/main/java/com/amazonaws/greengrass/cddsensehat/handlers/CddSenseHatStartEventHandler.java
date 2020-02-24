package com.amazonaws.greengrass.cddsensehat.handlers;

import com.amazonaws.greengrass.cddsensehat.data.Topics;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import com.amazonaws.greengrass.cddsensehat.leds.animation.runner.interfaces.AnimationRunner;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Set;

public class CddSenseHatStartEventHandler implements GreengrassLambdaEventHandler {
    private final Logger log = LoggerFactory.getLogger(CddSenseHatStartEventHandler.class);
    @Inject
    Set<Animation> animationSet;
    @Inject
    AnimationRunner animationRunner;
    @Inject
    Topics topics;

    @Inject
    public CddSenseHatStartEventHandler() {
    }

    @Override
    public boolean isTopicExpected(String topic) {
        return topic.startsWith(topics.getStartTopic());
    }

    @Override
    public void execute(ImmutableGreengrassLambdaEvent immutableGreengrassLambdaEvent) {
        String topic = immutableGreengrassLambdaEvent.getTopic().get();

        for (Animation animation : animationSet) {
            String animationName = animation.getClass().getSimpleName();
            String animationTopic = String.join("/", topics.getStartTopic(), animationName);
            log.info(String.join(" ", animationName, animationTopic));

            if (topic.equals(animationTopic)) {
                log.info("Starting [" + animationName + "]");
                animationRunner.startAnimation(animation);
                return;
            }
        }

        log.info("Message not understood on topic [" + topic + "]");
    }
}
