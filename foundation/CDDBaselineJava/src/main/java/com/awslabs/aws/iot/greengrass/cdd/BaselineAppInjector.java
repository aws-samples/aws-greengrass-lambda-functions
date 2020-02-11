package com.awslabs.aws.iot.greengrass.cdd;

import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import dagger.Component;
import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

@Singleton
@Component(modules = BaselineAppModule.class)
public interface BaselineAppInjector {
    EnvironmentProvider environmentProvider();

    EventBus eventBus();
}
