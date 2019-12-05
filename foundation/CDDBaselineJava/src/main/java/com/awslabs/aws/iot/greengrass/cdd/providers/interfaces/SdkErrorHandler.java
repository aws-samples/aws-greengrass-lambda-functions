package com.awslabs.aws.iot.greengrass.cdd.providers.interfaces;

import software.amazon.awssdk.core.exception.SdkClientException;

public interface SdkErrorHandler {
    Void handleSdkError(SdkClientException e);
}
