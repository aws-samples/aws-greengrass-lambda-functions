package com.awslabs.aws.iot.greengrass.cdd;

import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;

public interface BaselineInjector {
    Dispatcher dispatcher();

    EnvironmentProvider environmentProvider();
}
