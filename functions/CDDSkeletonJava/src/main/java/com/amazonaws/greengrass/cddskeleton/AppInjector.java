package com.amazonaws.greengrass.cddskeleton;

import com.amazonaws.greengrass.cddskeleton.handlers.InputHandler;
import com.amazonaws.greengrass.cddskeleton.handlers.StartupHandler;
import com.awslabs.aws.iot.greengrass.cdd.BaselineInjector;
import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = BaselineAppModule.class)
public interface AppInjector extends BaselineInjector {
    StartupHandler startupHandler();

    InputHandler inputHandler();
}
