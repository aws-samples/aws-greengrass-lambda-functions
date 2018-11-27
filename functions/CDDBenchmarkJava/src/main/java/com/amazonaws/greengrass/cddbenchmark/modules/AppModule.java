package com.amazonaws.greengrass.cddbenchmark.modules;

import com.amazonaws.greengrass.cddbenchmark.handlers.StartupHandler;
import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StartupHandler.class).asEagerSingleton();
    }
}
