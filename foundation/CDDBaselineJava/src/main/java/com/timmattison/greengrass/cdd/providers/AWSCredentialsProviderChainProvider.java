package com.timmattison.greengrass.cdd.providers;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.timmattison.greengrass.cdd.providers.interfaces.SafeProvider;
import com.timmattison.greengrass.cdd.providers.interfaces.SdkErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AWSCredentialsProviderChainProvider implements SafeProvider<AWSCredentialsProviderChain> {
    private final SdkErrorHandler sdkErrorHandler;

    @Override
    public AWSCredentialsProviderChain get() {
        return safeGet(sdkErrorHandler);
    }

    @Override
    public AWSCredentialsProviderChain unsafeGet() {
        return new DefaultAWSCredentialsProviderChain();
    }
}
