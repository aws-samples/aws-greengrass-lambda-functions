package com.awslabs.aws.iot.greengrass.cdd.helpers.implementations;

import com.awslabs.aws.iot.greengrass.cdd.events.GreengrassLambdaEvent;
import com.awslabs.aws.iot.greengrass.cdd.helpers.JsonHelper;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

public class BasicJsonHelper implements JsonHelper {
    private final Logger log = LoggerFactory.getLogger(BasicJsonHelper.class);

    @Inject
    public BasicJsonHelper() {
    }

    @Override
    public String toJson(Object object) {
        return getGsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create()
                .toJson(object);
    }

    private GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // This allows us to use GSON with Immutables - https://immutables.github.io/json.html#type-adapter-registration
        for (TypeAdapterFactory factory : ServiceLoader.load(TypeAdapterFactory.class)) {
            gsonBuilder.registerTypeAdapterFactory(factory);
        }

        return gsonBuilder;
    }

    @Override
    public <T> T fromJson(Class<T> clazz, byte[] json) {
        return fromJson(clazz, new String(json));
    }

    @Override
    public <T> T fromJson(Class<T> clazz, String json) {
        return getGsonBuilder().create()
                .fromJson(json, clazz);
    }

    @Override
    public <T> Optional<T> fromJson(Class<T> clazz, GreengrassLambdaEvent greengrassLambdaEvent) {
        if (!greengrassLambdaEvent.getJsonInput().isPresent()) {
            log.error("No JSON payload present for conversion to a [" + clazz.getName() + "]");
            return Optional.empty();
        }

        // Get the JSON input as a map
        Map input = greengrassLambdaEvent.getJsonInput().get();

        // Convert it back to a JSON string
        String jsonString = toJson(input);

        try {
            // Convert the JSON string to the requested class
            return Optional.ofNullable(fromJson(clazz, jsonString));
        } catch (Exception e) {
            log.error("Could not convert JSON [" + jsonString + "], to a [" + clazz.getName() + "]");
            return Optional.empty();
        }
    }
}
