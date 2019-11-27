package com.amazonaws.greengrass.cddlatencydashboard.modules;

import com.amazonaws.greengrass.cddlatencydashboard.handlers.InputHandler;
import com.amazonaws.greengrass.cddlatencydashboard.handlers.StartupHandler;
import com.amazonaws.greengrass.cddlatencydashboard.handlers.TimerFiredEventHandler;
import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StartupHandler.class).asEagerSingleton();
        bind(InputHandler.class).asEagerSingleton();

        bind(TimerFiredEventHandler.class).asEagerSingleton();
    }
}
