package com.amazonaws.greengrass.cddsensehat.handlers;

import com.amazonaws.greengrass.cddsensehat.data.Topics;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutablePublishObjectEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CddSenseHatListEventHandler implements GreengrassLambdaEventHandler {
    private final Logger log = LoggerFactory.getLogger(CddSenseHatListEventHandler.class);
    @Inject
    EventBus eventBus;
    @Inject
    Set<Animation> animationSet;
    @Inject
    Topics topics;

    @Inject
    public CddSenseHatListEventHandler() {
    }

    @Override
    public boolean isTopicExpected(String topic) {
        return topic.equals(topics.getListTopic());
    }

    @Override
    public void execute(GreengrassLambdaEvent greengrassLambdaEvent) {
        log.info("Routing to list...");

        Map<String, List<String>> animationList = new HashMap<>();

        animationList.put("animations",
                animationSet.stream()
                        .map(animation -> animation.getClass().getSimpleName())
                        .collect(Collectors.toList()));

        eventBus.post(ImmutablePublishObjectEvent.builder().topic(topics.getListOutputTopic()).object(animationList).build());
    }
}
