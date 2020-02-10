package com.awslabs.aws.iot.greengrass.cdd;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutablePublishMessageEvent;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import com.google.common.eventbus.EventBus;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public interface BaselineAppInterface {
    BaselineAppInjector baselineAppInjector = DaggerBaselineAppInjector.create();
    EnvironmentProvider environmentProvider = baselineAppInjector.environmentProvider();
    EventBus eventBus = baselineAppInjector.eventBus();

    static void initialize() {
        Instant initializeStart = Instant.now();
        Optional<String> region = environmentProvider.getRegion();

        if (!region.isPresent()) {
            System.err.println("Could not determine the region for this core.  aws.region system property not set.  TES may not work.");
        }

        region.ifPresent(theRegion -> System.setProperty("aws.region", theRegion));

        eventBus.post(ImmutableGreengrassStartEvent.builder().build());

        Instant initializeEnd = Instant.now();
        String debugTopic = String.join("/", environmentProvider.getAwsIotThingName().get(), "debug");
        eventBus.post(ImmutablePublishMessageEvent.builder().topic(debugTopic).message("Initialization took: " + Duration.between(initializeStart, initializeEnd).toString()).build());
    }

    default void handleBinaryRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        byte[] input = IOUtils.toByteArray(inputStream);
        String topic = getTopic(context);

        GreengrassLambdaEvent greengrassLambdaEvent = ImmutableGreengrassLambdaEvent.builder()
                .binaryInput(Optional.ofNullable(input))
                .topic(Optional.ofNullable(topic))
                .context(context)
                .logger(context.getLogger())
                .outputStream(Optional.ofNullable(outputStream))
                .build();

        eventBus.post(greengrassLambdaEvent);

        return;
    }

    default String getTopic(Context context) {
        return context.getClientContext().getCustom().get("subject");
    }

    /**
     * Legacy, default to JSON handler
     *
     * @param input
     * @param context
     * @return
     */
    default String handleRequest(Object input, Context context) {
        return handleJsonRequest(input, context);
    }

    default String handleJsonRequest(Object input, Context context) {
        LambdaLogger logger = context.getLogger();
        String topic = getTopic(context);

        Map map = null;

        if (input != null) {
            if (!(input instanceof LinkedHashMap)) {
                logger.log("Input could not be converted to a hashmap");
                return "";
            }

            map = (LinkedHashMap) input;
        }

        GreengrassLambdaEvent greengrassLambdaEvent = ImmutableGreengrassLambdaEvent.builder()
                .jsonInput(Optional.ofNullable(map))
                .topic(Optional.ofNullable(topic))
                .context(context)
                .logger(context.getLogger())
                .build();

        eventBus.post(greengrassLambdaEvent);

        return "";
    }
}
