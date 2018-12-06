package com.amazonaws.greengrass.cddembeddedvaadinskeleton.modules;

import com.amazonaws.greengrass.cddembeddedvaadinskeleton.handlers.InputHandler;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.handlers.StartupHandler;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.handlers.TimerFiredEventHandler;
import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StartupHandler.class).asEagerSingleton();
        bind(InputHandler.class).asEagerSingleton();

        bind(TimerFiredEventHandler.class).asEagerSingleton();
    }
}
