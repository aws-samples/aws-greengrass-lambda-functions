package com.amazonaws.greengrass.cddsensehat.leds;

import com.amazonaws.greengrass.cddsensehat.data.Topics;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutablePublishMessageEvent;
import com.google.common.eventbus.EventBus;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class LinuxOutputStreamWriterFactory implements OutputStreamWriterFactory {
    private final long minimumTimeBetweenErrors = 5000;
    @Inject
    EventBus eventBus;
    @Inject
    Topics topics;
    private long lastErrorSentTimestamp = 0;

    @Inject
    public LinuxOutputStreamWriterFactory() {

    }

    @Override
    public OutputStreamWriter get(File file, Charset charset) throws FileNotFoundException {
        try {
            return new OutputStreamWriter(new FileOutputStream(file), charset);
        } catch (FileNotFoundException e) {
            long now = System.currentTimeMillis();

            if ((now - lastErrorSentTimestamp) > minimumTimeBetweenErrors) {
                eventBus.post(ImmutablePublishMessageEvent.builder().topic(topics.getDebugOutputTopic()).message("File not found error reported for [" + file.getAbsolutePath() + "], has the local resource been associated with this function? Is the function running inside the Greengrass container?").build());
                lastErrorSentTimestamp = now;
            }

            throw e;
        }
    }
}
