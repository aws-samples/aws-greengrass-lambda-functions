package com.awslabs.aws.iot.greengrass.cdd.providers;

import com.awslabs.aws.iot.greengrass.cdd.providers.GreengrassSdkErrorHandler;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.SdkErrorHandler;
import io.vavr.control.Try;
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

        return Try.of(callable::call)
                .recover(SdkClientException.class, throwable -> (T) sdkErrorHandler.handleSdkError(throwable))
                .get();
    }
}
