package com.amazonaws.greengrass.cddkinesis.handlers;

import com.amazonaws.greengrass.cddkinesis.data.Topics;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutablePublishMessageEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;
import com.google.common.eventbus.EventBus;
import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pipeline;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

public class StartupHandler implements GreengrassStartEventHandler {
    private static final int START_DELAY_MS = 5000;
    private static final int PERIOD_MS = 5000;
    @Inject
    EventBus eventBus;
    @Inject
    Topics topics;

    @Inject
    public StartupHandler() {
    }

    /**
     * Receives the Greengrass start event from the event bus, publishes a message indicating it has started, and creates
     * a timer that publishes a message every 5 seconds after a 5 second delay
     *
     * @param greengrassStartEvent
     */
    @Override
    public void execute(GreengrassStartEvent greengrassStartEvent) {
        eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getOutputTopic()).message("Skeleton started [" + System.currentTimeMillis() + "]").build());

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getOutputTopic()).message("Skeleton still running, test 2... [" + System.currentTimeMillis() + "]").build());
            }
        }, START_DELAY_MS, PERIOD_MS);


        Runnable myRunnable =
            new Runnable(){
                public void run(){
                    Gst.init("CameraTest");
                    Bin bin = Gst.parseBinFromDescription(
                            "v4l2src do-timestamp=TRUE device=/dev/video0 ! videoconvert " +
                                    "! video/x-raw,format=I420,width=640,height=480,framerate=15/1 " +
                                    "! omxh264enc periodicty-idr=45 inline-header=FALSE ! h264parse " +
                                    "! video/x-h264,stream-format=avc,alignment=au " +
                                    "! kvssink stream-name=MyExample access-key=\"AKIA2SFZUBGVOHU7AHFZ\" " +
                                    "secret-key=\"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\" aws-region=\"eu-west-1\"",
                            true);
                    Pipeline pipe = new Pipeline();
                    pipe.addMany(bin);//, vc.getElement());

                    pipe.play();

                    // Hack to keep this running, can this be ignored?
                    while (true) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        Thread thread = new Thread(myRunnable);
        thread.start();
    }
}
