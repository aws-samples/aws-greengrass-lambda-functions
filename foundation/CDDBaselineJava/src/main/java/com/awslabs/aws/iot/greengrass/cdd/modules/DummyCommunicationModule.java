package com.awslabs.aws.iot.greengrass.cdd.modules;

import com.awslabs.aws.iot.greengrass.cdd.communication.Communication;
import com.awslabs.aws.iot.greengrass.cdd.communication.DummyCommunication;
import com.awslabs.aws.iot.greengrass.cdd.providers.DummyEnvironmentProvider;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import com.google.inject.AbstractModule;

public class DummyCommunicationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Communication.class).to(DummyCommunication.class).asEagerSingleton();

        bind(EnvironmentProvider.class).to(DummyEnvironmentProvider.class);
    }
}