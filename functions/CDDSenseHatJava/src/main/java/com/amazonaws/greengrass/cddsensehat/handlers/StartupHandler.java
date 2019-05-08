package com.amazonaws.greengrass.cddsensehat.handlers;

import com.amazonaws.greengrass.cddsensehat.events.TimerFiredEvent;
import com.google.common.eventbus.EventBus;
import com.timmattison.greengrass.cdd.events.GreengrassStartEvent;
import com.timmattison.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class StartupHandler implements GreengrassStartEventHandler {
    private final int DELAY_MS = 5000;
    private final int PERIOD_MS = 5000;
    private final EventBus eventBus;

    @Override
    public void execute(GreengrassStartEvent greengrassStartEvent) {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                eventBus.post(new TimerFiredEvent());
            }
        }, DELAY_MS, PERIOD_MS);
    }
}
