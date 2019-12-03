package com.amazonaws.greengrass.cddlatencydashboard.helpers;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

import java.util.ServiceLoader;

public class JsonHelper {
    public String toJson(Object object) {
        return getGsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create()
                .toJson(object);
    }

    private GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        for (TypeAdapterFactory factory : ServiceLoader.load(TypeAdapterFactory.class)) {
            gsonBuilder.registerTypeAdapterFactory(factory);
        }

        return gsonBuilder;
    }

    public <T> T fromJson(Class<T> clazz, byte[] json) {
        return getGsonBuilder().create()
                .fromJson(new String(json), clazz);
    }
}
