package com.awslabs.aws.iot.greengrass.cdd.providers;

import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.SdkErrorHandler;
import software.amazon.awssdk.core.exception.SdkClientException;

import javax.inject.Provider;
import java.util.concurrent.Callable;

public class SafeProvider<T> implements Provider<T> {
    private final Callable<T> callable;

    public SafeProvider(Callable<T> callable) {
        this.callable = callable;
    }

    @Override
    public T get() {
        SdkErrorHandler sdkErrorHandler = new GreengrassSdkErrorHandler();

        try {
            return callable.call();
        } catch (SdkClientException sdkClientException) {
            sdkErrorHandler.handleSdkError(sdkClientException);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("Should never get here");
    }
}
