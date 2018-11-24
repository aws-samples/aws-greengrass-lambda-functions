package com.timmattison.greengrass.cdd.providers.interfaces;

import com.amazonaws.SdkClientException;

import javax.inject.Provider;

public interface SafeProvider<T> extends Provider<T> {
    default T safeGet(SdkErrorHandler sdkErrorHandler) {
        try {
            return unsafeGet();
        } catch (SdkClientException e) {
            sdkErrorHandler.handleSdkError(e);
        }

        throw new UnsupportedOperationException("You should never reach here, this is a bug");
    }

    T unsafeGet();
}
