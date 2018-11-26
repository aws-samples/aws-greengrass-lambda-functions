package com.amazonaws.greengrass.cdddmi.handlers;

import com.amazonaws.greengrass.cdddmi.data.Topics;
import com.google.common.eventbus.EventBus;
import com.timmattison.greengrass.cdd.events.GreengrassStartEvent;
import com.timmattison.greengrass.cdd.events.PublishMessageEvent;
import com.timmattison.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class StartupHandler implements GreengrassStartEventHandler {
    private final EventBus eventBus;
    private final Topics topics;

    /**
     * Receives the Greengrass start event from the event bus, publishes a message indicating it has started, and creates
     * a timer that publishes a message every 5 seconds after a 5 second delay
     *
     * @param greengrassStartEvent
     */
    @Override
    public void execute(GreengrassStartEvent greengrassStartEvent) {
        eventBus.post(PublishMessageEvent.builder().topic(topics.getOutputTopic()).message("DMI started [" + System.currentTimeMillis() + "]").build());
    }
}
