package com.timmattison.greengrass.cdd.modules;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.timmattison.greengrass.cdd.BaselineAppInterface;
import com.timmattison.greengrass.cdd.nativeprocesses.TempDirNativeProcessHelper;
import com.timmattison.greengrass.cdd.nativeprocesses.interfaces.NativeProcessHelper;
import com.timmattison.greengrass.cdd.providers.AWSCredentialsProviderChainProvider;
import com.timmattison.greengrass.cdd.providers.BasicEnvironmentProvider;
import com.timmattison.greengrass.cdd.providers.GreengrassSdkErrorHandler;
import com.timmattison.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import com.timmattison.greengrass.cdd.providers.interfaces.SdkErrorHandler;

public class BaselineAppModule extends AbstractModule {
    @Override
    protected void configure() {
        // Use a Guava event bus
        bind(EventBus.class).toInstance(BaselineAppInterface.eventBus);

        // Find all @Subscribe annotations and bind them to the event bus
        bindListener(Matchers.any(), new TypeListener() {
            public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
                // Method reference version
                typeEncounter.register((InjectionListener<I>) BaselineAppInterface.eventBus::register);
            }
        });

        // Methods to help users launch native processes
        bind(NativeProcessHelper.class).to(TempDirNativeProcessHelper.class);

        // Special environment information (thing name, thing ARN, group name);
        bind(EnvironmentProvider.class).to(BasicEnvironmentProvider.class);

        // Error handler to help debugging TES and SDK issues
        bind(SdkErrorHandler.class).to(GreengrassSdkErrorHandler.class);

        // Use a default credentials provider chain
        bind(AWSCredentialsProviderChain.class).toProvider(AWSCredentialsProviderChainProvider.class);
    }
}
