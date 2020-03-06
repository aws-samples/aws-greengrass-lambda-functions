package com.amazonaws.greengrass.cddkvs.handlers;

import com.amazonaws.greengrass.cddkvs.data.Topics;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.handlers.interfaces.GreengrassStartEventHandler;
import com.awslabs.aws.iot.greengrass.cdd.nativeprocesses.interfaces.NativeProcessHelper;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.glib.GLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StartupHandler implements GreengrassStartEventHandler {
    private static final int START_DELAY_MS = 5000;
    private static final int PERIOD_MS = 5000;
    public static final String GST_PLUGIN_PATH_NAME = "GST_PLUGIN_PATH";
    public static final String LIBGSTKVSSINK_SO_NAME = "libgstkvssink.so";
    private final Logger log = LoggerFactory.getLogger(StartupHandler.class);
    private String uuid = UUID.randomUUID().toString();
    private Pipeline pipe = null;
    @Inject
    Topics topics;
    @Inject
    DefaultCredentialsProvider defaultCredentialsProvider;
    @Inject
    EnvironmentProvider environmentProvider;
    @Inject
    NativeProcessHelper nativeProcessHelper;
    @Inject
    Dispatcher dispatcher;

    @Inject
    public StartupHandler() {
    }

    /**
     * Receives the Greengrass start event from the event bus, publishes a message indicating it has started, and creates
     * a timer that publishes a message every 5 seconds after a 5 second delay
     *
     * @param immutableGreengrassStartEvent
     */
    @Override
    public void execute(ImmutableGreengrassStartEvent immutableGreengrassStartEvent) {
        dispatcher.publishMessageEvent(topics.getOutputTopic(), "KVS streamer started [" + System.nanoTime() + "] [" + uuid + "]");

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            private long previousSeconds = Long.MIN_VALUE;

            @Override
            public void run() {
                dispatcher.publishMessageEvent(topics.getOutputTopic(), "KVS streamer still running [" + System.nanoTime() + "] [" + uuid + "]");

                if (pipe == null) {
                    dispatcher.publishMessageEvent(topics.getOutputTopic(), "No pipe yet [" + System.nanoTime() + "] [" + uuid + "]");
                    return;
                }

                long seconds = pipe.queryPosition(TimeUnit.SECONDS);

                if (seconds == previousSeconds) {
                    dispatcher.publishMessageEvent(topics.getOutputTopic(), "Pipe is stalled, restarting [" + seconds + "] [" + System.nanoTime() + "] [" + uuid + "]");
                    System.exit(1);
                }

                previousSeconds = seconds;
                dispatcher.publishMessageEvent(topics.getOutputTopic(), "Pipe state [" + pipe.getState().name() + "] [" + seconds + "] [" + System.nanoTime() + "] [" + uuid + "]");
            }
        }, START_DELAY_MS, PERIOD_MS);

        // Start the video pipeline in a separate thread so that we can return from this function as soon as possible
        new Thread(this::startVideoPipeline).start();
    }

    private void startVideoPipeline() {
        Optional<String> optionalRegion = environmentProvider.getRegion();

        if (!optionalRegion.isPresent()) {
            log.error("Region is not available, can not start the KVS function");
            return;
        }

        String region = optionalRegion.get();

        Optional<String> optionalStreamName = environmentProvider.get("STREAM_NAME");

        if (!optionalStreamName.isPresent()) {
            log.error("Stream name is not available, can not start the KVS function");
            return;
        }

        String streamName = optionalStreamName.get();

        Optional<String> optionalGstPluginPath = environmentProvider.get(GST_PLUGIN_PATH_NAME);

        if (!optionalGstPluginPath.isPresent()) {
            // Find the plugin for them
            List<Path> availablePaths = nativeProcessHelper.listAllFiles();

            Optional<Path> optionalKvsSinkLocation = availablePaths.stream()
                    .filter(path -> path.endsWith(LIBGSTKVSSINK_SO_NAME))
                    .findFirst();

            if (!optionalKvsSinkLocation.isPresent()) {
                log.error("Plugin path not specified and the KVS sink plugin could not be found, can not start the KVS function");
                return;
            }

            Path kvsSinkLocation = optionalKvsSinkLocation.get();
            optionalGstPluginPath = Optional.of(kvsSinkLocation.getParent().toAbsolutePath().toString());
            log.error("Plugin path not specified but the KVS sink plugin was found [" + kvsSinkLocation.toAbsolutePath() + "], setting " + GST_PLUGIN_PATH_NAME + " to [" + optionalGstPluginPath.get() + "]");
        }

        optionalGstPluginPath.ifPresent(gstPluginPath -> GLib.setEnv(GST_PLUGIN_PATH_NAME, gstPluginPath, true));

        AwsCredentials credentials = defaultCredentialsProvider.resolveCredentials();
        String temporaryAccessKey = credentials.accessKeyId();
        String temporarySecretKey = credentials.secretAccessKey();

        if (credentials instanceof AwsSessionCredentials) {
            GLib.setEnv("AWS_SESSION_TOKEN", ((AwsSessionCredentials) credentials).sessionToken(), true);
        }

        GLib.setEnv("AWS_ACCESS_KEY_ID", temporaryAccessKey, true);
        GLib.setEnv("AWS_SECRET_ACCESS_KEY", temporarySecretKey, true);
        GLib.setEnv("AWS_DEFAULT_REGION", region, true);

        StringBuilder commandStringBuilder = new StringBuilder();
        commandStringBuilder.append("v4l2src do-timestamp=TRUE device=/dev/video0 ! videoconvert ");
        commandStringBuilder.append("! video/x-raw,format=I420,width=640,height=480,framerate=15/1 ");
        commandStringBuilder.append("! omxh264enc periodicty-idr=45 inline-header=FALSE ! h264parse ");
        commandStringBuilder.append("! video/x-h264,stream-format=avc,alignment=au ");
        commandStringBuilder.append("! kvssink stream-name=");
        commandStringBuilder.append(streamName);

        String commandString = commandStringBuilder.toString();

        Gst.init("CDDKVSJava");
        Bin bin = Gst.parseBinFromDescription(commandString, true);

        pipe = new Pipeline();
        pipe.addMany(bin);

        pipe.play();
    }
}
