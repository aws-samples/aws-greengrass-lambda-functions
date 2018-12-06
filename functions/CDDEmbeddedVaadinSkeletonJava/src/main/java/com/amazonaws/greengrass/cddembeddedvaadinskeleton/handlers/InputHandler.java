package com.amazonaws.greengrass.cddembeddedvaadinskeleton.handlers;

import com.amazonaws.greengrass.cddembeddedvaadinskeleton.data.Topics;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.events.MessageFromCloudEvent;
import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.timmattison.greengrass.cdd.events.GreengrassLambdaEvent;
import com.timmattison.greengrass.cdd.handlers.interfaces.GreengrassLambdaEventHandler;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InputHandler implements GreengrassLambdaEventHandler {
    private final EventBus eventBus;
    private final Topics topics;

    @Override
    public boolean isTopicExpected(String topic) {
        return topic.equals(topics.getInputTopic());
    }

    @Override
    public void execute(GreengrassLambdaEvent greengrassLambdaEvent) {
        eventBus.post(MessageFromCloudEvent.builder().topic(greengrassLambdaEvent.getTopic().get()).message(new Gson().toJson(greengrassLambdaEvent.getInput())).build());
    }
}
