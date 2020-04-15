package com.amazonaws.greengrass.cddembeddedvaadinskeleton.data;

import com.awslabs.aws.iot.greengrass.cdd.data.CddTopics;

import javax.inject.Inject;
import java.util.Optional;

public class Topics {
    @Inject
    CddTopics cddTopics;

    private Optional<String> baselineTopic = Optional.empty();
    private Optional<String> inputTopic = Optional.empty();
    private Optional<String> outputTopic = Optional.empty();

    @Inject
    public Topics() {
    }

    private String getBaselineTopic() {
        if (!baselineTopic.isPresent()) {
            baselineTopic = Optional.of(cddTopics.getCddDriverTopic(this));
        }

        return baselineTopic.get();
    }

    public String getInputTopic() {
        if (!inputTopic.isPresent()) {
            inputTopic = Optional.of(String.join("/", getBaselineTopic(), "input"));
        }

        return inputTopic.get();
    }

    public String getOutputTopic() {
        if (!outputTopic.isPresent()) {
            outputTopic = Optional.of(String.join("/", getBaselineTopic(), "output"));
        }

        return outputTopic.get();
    }
}
