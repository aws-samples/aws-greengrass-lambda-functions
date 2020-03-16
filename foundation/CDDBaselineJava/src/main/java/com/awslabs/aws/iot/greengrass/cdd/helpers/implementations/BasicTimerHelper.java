package com.awslabs.aws.iot.greengrass.cdd.helpers.implementations;

import com.awslabs.aws.iot.greengrass.cdd.helpers.TimerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class BasicTimerHelper implements TimerHelper {
    private final Logger log = LoggerFactory.getLogger(BasicTimerHelper.class);

    @Override
    public ScheduledFuture<?> scheduleRunnable(Runnable runnable, int startDelay, int period, TimeUnit timeUnit) {
        Runnable safeRunnable = () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        };

        return Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(safeRunnable, startDelay, period, TimeUnit.MILLISECONDS);
    }
}
