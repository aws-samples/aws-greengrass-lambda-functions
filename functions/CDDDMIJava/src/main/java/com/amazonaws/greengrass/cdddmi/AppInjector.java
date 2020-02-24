package com.amazonaws.greengrass.cdddmi;

import com.amazonaws.greengrass.cdddmi.handlers.RequestHandler;
import com.amazonaws.greengrass.cdddmi.handlers.StartupHandler;
import com.awslabs.aws.iot.greengrass.cdd.BaselineInjector;
import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {BaselineAppModule.class, AppModule.class})
public interface AppInjector extends BaselineInjector {
    StartupHandler startupHandler();

    RequestHandler requestHandler();
}
