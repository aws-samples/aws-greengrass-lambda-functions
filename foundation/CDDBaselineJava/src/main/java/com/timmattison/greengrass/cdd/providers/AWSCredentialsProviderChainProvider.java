package com.timmattison.greengrass.cdd.providers;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.timmattison.greengrass.cdd.providers.interfaces.SafeProvider;
import com.timmattison.greengrass.cdd.providers.interfaces.SdkErrorHandler;

import javax.inject.Inject;

public class AWSCredentialsProviderChainProvider implements SafeProvider<AWSCredentialsProviderChain> {
    @Inject
    private final SdkErrorHandler sdkErrorHandler;

    public AWSCredentialsProviderChainProvider(SdkErrorHandler sdkErrorHandler) {
        this.sdkErrorHandler = sdkErrorHandler;
    }

    @Override
    public AWSCredentialsProviderChain get() {
        return safeGet(sdkErrorHandler);
    }

    @Override
    public AWSCredentialsProviderChain unsafeGet() {
        return new DefaultAWSCredentialsProviderChain();
    }
}
