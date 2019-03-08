package com.amazonaws.greengrass.cddsensehat.handlers;

import com.amazonaws.greengrass.cddsensehat.data.Topics;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import com.amazonaws.greengrass.cddsensehat.leds.animation.runner.interfaces.AnimationRunner;
import com.timmattison.greengrass.cdd.events.GreengrassLambdaEvent;
import com.timmattison.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CddSenseHatStartEventHandler implements GreengrassLambdaEventHandler {
    private final Set<Animation> animationSet;
    private final AnimationRunner animationRunner;
    private final Topics topics;

    @Override
    public boolean isTopicExpected(String topic) {
        return topic.startsWith(topics.getStartTopic());
    }

    @Override
    public void execute(GreengrassLambdaEvent greengrassLambdaEvent) {
        String topic = greengrassLambdaEvent.getTopic().get();

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
