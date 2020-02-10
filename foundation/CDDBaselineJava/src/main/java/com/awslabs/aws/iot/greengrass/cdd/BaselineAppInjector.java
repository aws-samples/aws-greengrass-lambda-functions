package com.awslabs.aws.iot.greengrass.cdd;

import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import com.google.common.eventbus.EventBus;
import dagger.Component;

@Component(modules = BaselineAppModule.class)
public interface BaselineAppInjector {
    EnvironmentProvider environmentProvider();

    EventBus eventBus();
}
