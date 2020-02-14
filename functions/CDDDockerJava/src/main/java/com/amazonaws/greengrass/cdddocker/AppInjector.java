package com.amazonaws.greengrass.cdddocker;

import com.amazonaws.greengrass.cdddocker.handlers.*;
import com.awslabs.aws.iot.greengrass.cdd.BaselineInjector;
import com.awslabs.aws.iot.greengrass.cdd.modules.BaselineAppModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {BaselineAppModule.class, AppModule.class})
public interface AppInjector extends BaselineInjector {
    StartupHandler startupHandler();

    RequestHandler requestHandler();

    DockerListRequestHandler dockerListRequestHandler();

    DockerPullRequestHandler dockerPullRequestHandler();

    DockerRunRequestHandler dockerRunRequestHandler();

    DockerStopRequestHandler dockerStopRequestHandler();
}
