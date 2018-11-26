package com.amazonaws.greengrass.cdddmi.modules;

import com.amazonaws.greengrass.cdddmi.data.Topics;
import com.amazonaws.greengrass.cdddmi.dmi.BasicDmiFetcher;
import com.amazonaws.greengrass.cdddmi.dmi.interfaces.DmiFetcher;
import com.amazonaws.greengrass.cdddmi.handlers.InputHandler;
import com.amazonaws.greengrass.cdddmi.handlers.StartupHandler;
import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StartupHandler.class).asEagerSingleton();
        bind(InputHandler.class).asEagerSingleton();
        bind(Topics.class);

        bind(DmiFetcher.class).to(BasicDmiFetcher.class);
    }
}