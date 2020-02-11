package com.awslabs.aws.iot.greengrass.cdd;

import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = BaselineAppModule.class)
public interface BaselineAppInjector {
    EnvironmentProvider environmentProvider();

    @Singleton
    Dispatcher dispatcher();
}
