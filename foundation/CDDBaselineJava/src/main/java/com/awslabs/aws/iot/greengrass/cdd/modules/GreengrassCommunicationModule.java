package com.awslabs.aws.iot.greengrass.cdd.modules;

import com.amazonaws.greengrass.javasdk.IotDataClient;
import com.amazonaws.greengrass.javasdk.LambdaClient;
import com.awslabs.aws.iot.greengrass.cdd.communication.Communication;
import com.awslabs.aws.iot.greengrass.cdd.communication.GreengrassCommunication;
import com.awslabs.aws.iot.greengrass.cdd.providers.BasicEnvironmentProvider;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import com.google.inject.AbstractModule;

public class GreengrassCommunicationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IotDataClient.class);
        bind(LambdaClient.class);

        bind(Communication.class).to(GreengrassCommunication.class).asEagerSingleton();

        bind(EnvironmentProvider.class).to(BasicEnvironmentProvider.class);
    }
}
