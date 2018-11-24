package com.amazonaws.greengrass.cddskeleton.modules;

import com.amazonaws.greengrass.cddskeleton.handlers.InputHandler;
import com.amazonaws.greengrass.cddskeleton.handlers.StartupHandler;
import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        // Any class that needs to receive messages must be set up as an eager singleton so Guice will autowire them at startup

        // StartupHandler will get a message indicating that the core has started
        bind(StartupHandler.class).asEagerSingleton();

        // InputHandler will only call its execute method on messages that are from the Topics.inputTopic topic
        bind(InputHandler.class).asEagerSingleton();
    }
}
