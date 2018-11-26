package com.amazonaws.greengrass.cdddmi.handlers;

import com.amazonaws.greengrass.cdddmi.data.Topics;
import com.amazonaws.greengrass.cdddmi.dmi.interfaces.DmiFetcher;
import com.google.common.eventbus.EventBus;
import com.timmattison.greengrass.cdd.events.GreengrassLambdaEvent;
import com.timmattison.greengrass.cdd.events.PublishObjectEvent;
import com.timmattison.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InputHandler implements GreengrassLambdaEventHandler {
    private final EventBus eventBus;
    private final DmiFetcher dmiFetcher;
    private final Topics topics;

    @Override
    public boolean isTopicExpected(String topic) {
        return topic.equals(topics.getInputTopic());
    }

    @Override
    public void execute(GreengrassLambdaEvent greengrassLambdaEvent) {
        eventBus.post(PublishObjectEvent.builder().topic(topics.getOutputTopic()).object(dmiFetcher.fetch().get()).build());
    }
}
