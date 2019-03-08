package com.amazonaws.greengrass.cddsensehat.handlers;

import com.amazonaws.greengrass.cddsensehat.data.Topics;
import com.amazonaws.greengrass.cddsensehat.leds.animation.interfaces.Animation;
import com.google.common.eventbus.EventBus;
import com.timmattison.greengrass.cdd.events.GreengrassLambdaEvent;
import com.timmattison.greengrass.cdd.events.PublishObjectEvent;
import com.timmattison.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CddSenseHatListEventHandler implements GreengrassLambdaEventHandler {
    private final EventBus eventBus;
    private final Set<Animation> animationSet;
    private final Topics topics;

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

        eventBus.post(PublishObjectEvent.builder().topic(topics.getListOutputTopic()).object(animationList).build());
    }
}
