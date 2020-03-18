package com.awslabs.aws.iot.greengrass.cdd.data;

import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;

import javax.inject.Inject;
import java.util.Optional;

public class CddTopics {
    @Inject
    EnvironmentProvider environmentProvider;

    @Inject
    public CddTopics() {
    }

    private Optional<String> cddBaselineTopic = Optional.empty();

    public String getCddBaselineTopic() {
        if (!cddBaselineTopic.isPresent()) {
            cddBaselineTopic = Optional.of(String.join("/",
                    environmentProvider.getAwsIotThingName().orElseThrow(() -> new UnsupportedOperationException("Thing name missing from environment")),
                    "cdd"));
        }

        return cddBaselineTopic.get();
    }

    public String debugTopic() {
        return String.join("/", getCddBaselineTopic(), "debug");
    }

    public String getCddDriverTopic(Object object) {
        return String.join("/",
                getCddBaselineTopic(),
                environmentProvider.getDriverName(object.getClass()));
    }
}
