package com.timmattison.greengrass.cdd.modules;

import com.google.inject.AbstractModule;
import com.timmattison.greengrass.cdd.communication.Communication;
import com.timmattison.greengrass.cdd.communication.DummyCommunication;
import com.timmattison.greengrass.cdd.providers.DummyEnvironmentProvider;
import com.timmattison.greengrass.cdd.providers.interfaces.EnvironmentProvider;

public class DummyCommunicationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Communication.class).to(DummyCommunication.class).asEagerSingleton();

        bind(EnvironmentProvider.class).to(DummyEnvironmentProvider.class);
    }
}