package com.amazonaws.greengrass.cddsensehat.data;

import com.awslabs.aws.iot.greengrass.cdd.data.CddTopics;

import javax.inject.Inject;
import java.util.Optional;

public class Topics {
    @Inject
    CddTopics cddTopics;
    private Optional<String> animationTopic = Optional.empty();
    private Optional<String> startTopic = Optional.empty();
    private Optional<String> stopTopic = Optional.empty();
    private Optional<String> listTopic = Optional.empty();
    private Optional<String> listOutputTopic = Optional.empty();
    private Optional<String> debugOutputTopic = Optional.empty();
    @Inject
    public Topics() {
    }

    private String getAnimationTopic() {
        if (!animationTopic.isPresent()) {
            animationTopic = Optional.of(String.join("/", cddTopics.getCddDriverTopic(this), "animation"));
        }

        return animationTopic.get();
    }

    public String getStartTopic() {
        if (!startTopic.isPresent()) {
            startTopic = Optional.of(String.join("/", getAnimationTopic(), "start"));
        }

        return startTopic.get();
    }

    public String getStopTopic() {
        if (!stopTopic.isPresent()) {
            stopTopic = Optional.of(String.join("/", getAnimationTopic(), "stop"));
        }

        return stopTopic.get();
    }

    public String getListTopic() {
        if (!listTopic.isPresent()) {
            listTopic = Optional.of(String.join("/", getAnimationTopic(), "list"));
        }

        return listTopic.get();
    }

    public String getListOutputTopic() {
        if (!listOutputTopic.isPresent()) {
            listOutputTopic = Optional.of(String.join("/", getListTopic(), "output"));
        }

        return listOutputTopic.get();
    }

    public String getDebugOutputTopic() {
        if (!debugOutputTopic.isPresent()) {
            debugOutputTopic = Optional.of(String.join("/", cddTopics.getCddDriverTopic(this), "debug"));
        }

        return debugOutputTopic.get();
    }
}
