package com.amazonaws.greengrass.cddbenchmark.data;

import com.awslabs.aws.iot.greengrass.cdd.data.CddTopics;

import javax.inject.Inject;
import java.util.EmptyStackException;
import java.util.Optional;

public class Topics {
    @Inject
    CddTopics cddTopics;

    @Inject
    public Topics() {
    }

    private Optional<String> baselineTopic = Optional.empty();
    private Optional<String> resultsTopic = Optional.empty();
    private Optional<String> outputTopic = Optional.empty();

    public String getBaselineTopic() {
        if (!baselineTopic.isPresent()) {
            baselineTopic = Optional.of(cddTopics.getCddDriverTopic(this));
        }

        return baselineTopic.get();
    }

    public String getResultsTopic() {
        if (!resultsTopic.isPresent()) {
            resultsTopic = Optional.of(String.join("/", getBaselineTopic(), "results", "java"));
        }

        return resultsTopic.get();
    }
    public String getOutputTopic() {
        if (!outputTopic.isPresent()) {
            outputTopic = Optional.of(String.join("/", getBaselineTopic(), "output", "java"));
        }

        return outputTopic.get();
    }
}
