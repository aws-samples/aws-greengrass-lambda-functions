package com.timmattison.greengrass.cdd.modules;

import com.amazonaws.greengrass.javasdk.IotDataClient;
import com.amazonaws.greengrass.javasdk.LambdaClient;
import com.google.inject.AbstractModule;
import com.timmattison.greengrass.cdd.communication.Communication;
import com.timmattison.greengrass.cdd.communication.GreengrassCommunication;
import com.timmattison.greengrass.cdd.providers.BasicEnvironmentProvider;
import com.timmattison.greengrass.cdd.providers.interfaces.EnvironmentProvider;

public class GreengrassCommunicationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IotDataClient.class);
        bind(LambdaClient.class);

        bind(Communication.class).to(GreengrassCommunication.class).asEagerSingleton();

        bind(EnvironmentProvider.class).to(BasicEnvironmentProvider.class);
    }
}
