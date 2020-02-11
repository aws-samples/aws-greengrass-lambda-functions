package com.amazonaws.greengrass.cddskeleton;

import com.amazonaws.greengrass.cddskeleton.handlers.InputHandler;
import com.amazonaws.greengrass.cddskeleton.handlers.StartupHandler;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = BaselineAppModule.class)
public interface AppInjector {
    StartupHandler startupHandler();

    InputHandler inputHandler();

    Dispatcher dispatcher();

    EnvironmentProvider environmentProvider();
}
