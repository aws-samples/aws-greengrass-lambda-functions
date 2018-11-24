package com.amazonaws.greengrass.cddskeleton.handlers;

import com.amazonaws.greengrass.cddskeleton.data.Topics;
import com.google.common.eventbus.EventBus;
import com.timmattison.greengrass.cdd.events.GreengrassStartEvent;
import com.timmattison.greengrass.cdd.events.PublishMessageEvent;
import com.timmattison.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class StartupHandler implements GreengrassStartEventHandler {
    public static final int START_DELAY_MS = 5000;
    public static final int PERIOD_MS = 5000;
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
        eventBus.post(PublishMessageEvent.builder().topic(topics.getOutputTopic()).message("Skeleton started [" + System.currentTimeMillis() + "]").build());

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                eventBus.post(PublishMessageEvent.builder().topic(topics.getOutputTopic()).message("Skeleton still running... [" + System.currentTimeMillis() + "]").build());
            }
        }, START_DELAY_MS, PERIOD_MS);
    }
}
