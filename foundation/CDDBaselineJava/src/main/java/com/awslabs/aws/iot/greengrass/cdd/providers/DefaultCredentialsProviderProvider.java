package com.awslabs.aws.iot.greengrass.cdd.providers;

import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.SafeProvider;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.SdkErrorHandler;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

import javax.inject.Inject;

public class DefaultCredentialsProviderProvider implements SafeProvider<DefaultCredentialsProvider> {
    @Inject
    private SdkErrorHandler sdkErrorHandler;

    @Inject
    public DefaultCredentialsProviderProvider(SdkErrorHandler sdkErrorHandler) {
        this.sdkErrorHandler = sdkErrorHandler;
    }

    @Override
    public DefaultCredentialsProvider get() {
        return safeGet(sdkErrorHandler);
    }

    @Override
    public DefaultCredentialsProvider unsafeGet() {
        return DefaultCredentialsProvider.create();
    }
}
