package com.amazonaws.greengrass.cddsensehat.data;

import com.timmattison.greengrass.cdd.data.CddTopics;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Topics {
    private final CddTopics cddTopics;

    @Getter(lazy = true)
    private final String animationTopic = String.join("/", cddTopics.getCddDriverTopic(this), "animation");

    @Getter(lazy = true)
    private final String startTopic = String.join("/", getAnimationTopic(), "start");

    @Getter(lazy = true)
    private final String stopTopic = String.join("/", getAnimationTopic(), "stop");

    @Getter(lazy = true)
    private final String listTopic = String.join("/", getAnimationTopic(), "list");

    @Getter(lazy = true)
    private final String listOutputTopic = String.join("/", getListTopic(), "output");

    @Getter(lazy = true)
    private final String debugOutputTopic = String.join("/", cddTopics.getCddDriverTopic(this), "debug");
}
