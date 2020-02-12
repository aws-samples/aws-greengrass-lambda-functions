package com.amazonaws.greengrass.cddkinesis;

import com.amazonaws.greengrass.cddkinesis.handlers.InputHandler;
import com.amazonaws.greengrass.cddkinesis.handlers.StartupHandler;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {BaselineAppModule.class, AppModule.class})
public interface AppInjector {
    StartupHandler startupHandler();

    InputHandler inputHandler();

    Dispatcher dispatcher();

    EnvironmentProvider environmentProvider();
}
