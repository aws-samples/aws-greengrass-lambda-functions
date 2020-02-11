package com.awslabs.aws.iot.greengrass.cdd;

import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import com.google.common.eventbus.EventBus;
import dagger.Component;
import dagger.Subcomponent;

import javax.inject.Singleton;

@Singleton
@Subcomponent(modules = BaselineAppModule.class)
public interface BaselineAppInjector {
    EnvironmentProvider environmentProvider();

    EventBus eventBus();
}
