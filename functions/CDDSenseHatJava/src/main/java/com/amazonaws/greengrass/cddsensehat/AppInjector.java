package com.amazonaws.greengrass.cddsensehat;

import com.amazonaws.greengrass.cddsensehat.handlers.CddSenseHatListEventHandler;
import com.amazonaws.greengrass.cddsensehat.handlers.CddSenseHatStartEventHandler;
import com.amazonaws.greengrass.cddsensehat.handlers.CddSenseHatStopEventHandler;
import com.amazonaws.greengrass.cddsensehat.handlers.StartupHandler;
import com.awslabs.aws.iot.greengrass.cdd.BaselineInjector;
import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {BaselineAppModule.class, AppModule.class})
public interface AppInjector extends BaselineInjector {
    StartupHandler startupHandler();

    CddSenseHatListEventHandler cddSenseHatListEventHandler();

    CddSenseHatStartEventHandler cddSenseHatStartEventHandler();

    CddSenseHatStopEventHandler cddSenseHatStopEventHandler();
}
