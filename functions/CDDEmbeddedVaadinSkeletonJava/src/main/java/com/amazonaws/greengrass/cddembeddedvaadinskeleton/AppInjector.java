package com.amazonaws.greengrass.cddembeddedvaadinskeleton;

import com.amazonaws.greengrass.cddembeddedvaadinskeleton.handlers.InputHandler;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.handlers.StartupHandler;
import com.amazonaws.greengrass.cddembeddedvaadinskeleton.handlers.TimerFiredEventHandler;
import com.awslabs.aws.iot.greengrass.cdd.BaselineInjector;
import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = BaselineAppModule.class)
public interface AppInjector extends BaselineInjector {
    StartupHandler startupHandler();

    InputHandler inputHandler();

    TimerFiredEventHandler timerFiredEventHandler();
}
