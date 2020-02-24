package com.amazonaws.greengrass.cddkvs;

import com.amazonaws.greengrass.cddkvs.handlers.StartupHandler;
import com.awslabs.aws.iot.greengrass.cdd.BaselineInjector;
import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {BaselineAppModule.class, AppModule.class})
public interface AppInjector extends BaselineInjector {
    StartupHandler startupHandler();
}
