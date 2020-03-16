package com.awslabs.aws.iot.greengrass.cdd.helpers;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface TimerHelper {
    ScheduledFuture<?> scheduleRunnable(Runnable runnable, int startDelay, int period, TimeUnit timeUnit);
}
