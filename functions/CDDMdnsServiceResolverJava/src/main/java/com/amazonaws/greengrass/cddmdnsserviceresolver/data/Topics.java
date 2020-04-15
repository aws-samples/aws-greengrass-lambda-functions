package com.amazonaws.greengrass.cddmdnsserviceresolver.data;

import com.awslabs.aws.iot.greengrass.cdd.data.CddTopics;

import javax.inject.Inject;
import java.util.Optional;

public class Topics {
    @Inject
    CddTopics cddTopics;

    private Optional<String> inputTopic = Optional.empty();
    private Optional<String> loggingTopic = Optional.empty();
    private Optional<String> serviceAddedTopic = Optional.empty();
    private Optional<String> serviceRemovedTopic = Optional.empty();
    private Optional<String> serviceResolvedTopic = Optional.empty();
    private Optional<String> baselineTopic = Optional.empty();

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

    public String getLoggingTopic() {
        if (!loggingTopic.isPresent()) {
            loggingTopic = Optional.of(String.join("/", getBaselineTopic(), "logging"));
        }

        return loggingTopic.get();
    }

    public String getServiceAddedTopic() {
        if (!serviceAddedTopic.isPresent()) {
            serviceAddedTopic = Optional.of(String.join("/", getBaselineTopic(), "added"));
        }

        return serviceAddedTopic.get();
    }

    public String getServiceRemovedTopic() {
        if (!serviceRemovedTopic.isPresent()) {
            serviceRemovedTopic = Optional.of(String.join("/", getBaselineTopic(), "removed"));
        }

        return serviceRemovedTopic.get();
    }

    public String getServiceResolvedTopic() {
        if (!serviceResolvedTopic.isPresent()) {
            serviceResolvedTopic = Optional.of(String.join("/", getBaselineTopic(), "resolved"));
        }

        return serviceResolvedTopic.get();
    }
}
