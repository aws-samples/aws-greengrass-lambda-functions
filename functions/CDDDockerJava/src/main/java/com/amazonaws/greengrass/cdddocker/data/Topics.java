package com.amazonaws.greengrass.cdddocker.data;

import com.awslabs.aws.iot.greengrass.cdd.data.CddTopics;

import javax.inject.Inject;

public class Topics {
    @Inject
    CddTopics cddTopics;

    @Inject
    public Topics() {
    }

    private String getBaselineTopic() {
        return cddTopics.getCddDriverTopic(this);
    }

    public String getRequestTopic() {
        return String.join("/", getBaselineTopic(), "request");
    }

    public String getResponseTopic() {
        return String.join("/", getBaselineTopic(), "response");
    }
}
