package com.timmattison.greengrass.cdd.data;

import com.timmattison.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CddTopics {
    private final EnvironmentProvider environmentProvider;

    @Getter(lazy = true)
    private final String cddBaselineTopic = buildCddBaselineTopic();

    private String buildCddBaselineTopic() {
        return String.join("/",
                environmentProvider.getAwsIotThingName().orElseThrow(() -> new UnsupportedOperationException("Thing name missing from environment")),
                "cdd");
    }

    public String getCddDriverTopic(Object object) {
        return String.join("/",
                buildCddBaselineTopic(),
                environmentProvider.getDriverName(object.getClass()));
    }
}
