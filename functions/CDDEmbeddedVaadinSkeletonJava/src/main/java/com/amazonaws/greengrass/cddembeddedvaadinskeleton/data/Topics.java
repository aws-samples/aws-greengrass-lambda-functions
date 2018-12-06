package com.amazonaws.greengrass.cddembeddedvaadinskeleton.data;

import com.timmattison.greengrass.cdd.data.CddTopics;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Topics {
    private final CddTopics cddTopics;

    @Getter(lazy = true)
    private final String baselineTopic = cddTopics.getCddDriverTopic(this);

    @Getter(lazy = true)
    private final String inputTopic = String.join("/", getBaselineTopic(), "input");

    @Getter(lazy = true)
    private final String outputTopic = String.join("/", getBaselineTopic(), "output");
}
