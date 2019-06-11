package com.amazonaws.greengrass.cddskeleton.data;

import com.awslabs.aws.iot.greengrass.cdd.data.CddTopics;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.inject.Inject;

@NoArgsConstructor
public class Topics {
    @Getter(lazy = true)
    private final String inputTopic = String.join("/", getBaselineTopic(), "input");
    @Getter(lazy = true)
    private final String outputTopic = String.join("/", getBaselineTopic(), "output");
    @Inject
    private CddTopics cddTopics;
    @Getter(lazy = true)
    private final String baselineTopic = cddTopics.getCddDriverTopic(this);
}
