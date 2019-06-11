package com.awslabs.aws.iot.greengrass.cdd;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutableGreengrassStartEvent;
import com.awslabs.aws.iot.greengrass.cdd.events.ImmutablePublishMessageEvent;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import com.awslabs.aws.iot.greengrass.cdd.modules.DummyCommunicationModule;
import com.awslabs.aws.iot.greengrass.cdd.modules.GreengrassCommunicationModule;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public interface BaselineAppInterface {
    EventBus eventBus = new EventBus();

    static Injector getInjector(List<AbstractModule> abstractModuleList) {
        try {
            ArrayList<AbstractModule> baselineModuleList = getBaselineModuleList(abstractModuleList);
            baselineModuleList.add(new GreengrassCommunicationModule());
            return Guice.createInjector(baselineModuleList);
        } catch (NoClassDefFoundError e) {
            // EnvVars will not be found when we call the connector task in a non-Greengrass environment
            ArrayList<AbstractModule> baselineModuleList = getBaselineModuleList(abstractModuleList);
            baselineModuleList.add(new DummyCommunicationModule());
            return Guice.createInjector(baselineModuleList);
        }
    }

    static ArrayList<AbstractModule> getBaselineModuleList(List<AbstractModule> abstractModuleList) {
        ArrayList<AbstractModule> tempModuleList = new ArrayList<>(abstractModuleList);
        tempModuleList.add(new BaselineAppModule());
        return tempModuleList;
    }

    static void initialize(List<AbstractModule> abstractModuleList) {
        Instant initializeStart = Instant.now();

        Injector injector = getInjector(abstractModuleList);

        Instant injectorEnd = Instant.now();

        EnvironmentProvider environmentProvider = injector.getInstance(EnvironmentProvider.class);
        Optional<String> region = environmentProvider.getRegion();

        if (!region.isPresent()) {
            System.err.println("Could not determine the region for this core.  aws.region system property not set.  TES may not work.");
        }

        region.ifPresent(theRegion -> System.setProperty("aws.region", theRegion));

        eventBus.post(ImmutableGreengrassStartEvent.builder().build());

        Instant initializeEnd = Instant.now();
        String debugTopic = String.join("/", environmentProvider.getAwsIotThingName().get(), "debug");
        eventBus.post(ImmutablePublishMessageEvent.builder().topic(debugTopic).message("Injector instantiation took: " + Duration.between(initializeStart, injectorEnd).toString()).build());
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
