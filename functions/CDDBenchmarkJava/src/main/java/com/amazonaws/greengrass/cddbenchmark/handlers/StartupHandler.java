package com.amazonaws.greengrass.cddbenchmark.handlers;

import com.amazonaws.greengrass.cddbenchmark.data.Topics;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class StartupHandler implements GreengrassStartEventHandler {
    private static final int START_DELAY_MS = 0;
    private static final int PERIOD_MS = 5000;
    // AtomicLongs for counters are overkill in the simple example but this code was watered down from a much more
    //   aggressive multithreaded test that I might revive later
    private static final AtomicLong messagesPublished = new AtomicLong(0);
    private static final AtomicLong errors = new AtomicLong(0);
    // Keep track of when the application started
    private static final long startTime = System.nanoTime();
    private final Logger log = LoggerFactory.getLogger(StartupHandler.class);
    @Inject
    Topics topics;
    @Inject
    Dispatcher dispatcher;

    @Inject
    public StartupHandler() {
    }

    @Override
    public void execute(ImmutableGreengrassStartEvent immutableGreengrassStartEvent) {
        createAndStartBenchmarkThread();

        createAndStartReportingTimer();
    }

    private void createAndStartReportingTimer() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    long endTime = System.nanoTime();
                    double messagesPerSecond;
                    double seconds = (endTime - startTime) / 1000.0;

                    Map<String, Number> map = new HashMap<>();

                    messagesPerSecond = (messagesPublished.doubleValue() / seconds);

                    map.put("messagesPublished", messagesPublished.get());
                    map.put("errors", errors);

                    map.put("messagesPerSecond", messagesPerSecond);
                    map.put("seconds", seconds);

                    dispatcher.publishObjectEvent(topics.getResultsTopic(), map);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }, START_DELAY_MS, PERIOD_MS);
    }

    private void createAndStartBenchmarkThread() {
        Map<String, String> benchmarkMessage = new HashMap<>();
        benchmarkMessage.put("message", "BENCHMARK");
        // Get the message as a byte array so we don't use Gson to convert it to a map each time
        byte[] benchmarkMessageBytes = new Gson().toJson(benchmarkMessage).getBytes();

        new Thread(() -> {
            Stream<Integer> infiniteStream = Stream
                    .iterate(0, i -> i + 1);

            infiniteStream
                    .forEach(i -> {
                        try {
                            dispatcher.publishBinaryEvent(topics.getOutputTopic(), benchmarkMessageBytes);

                            // Message was published successfully, increment the message counter
                            messagesPublished.incrementAndGet();
                        } catch (Exception e) {
                            // Message publish failed, increment the error counter
                            errors.incrementAndGet();

                            log.error(e.getMessage());
                            e.printStackTrace();
                        }
                    });
        }).start();
    }
}
