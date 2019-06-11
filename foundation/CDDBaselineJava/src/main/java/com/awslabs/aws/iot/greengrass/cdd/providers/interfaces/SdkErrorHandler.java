package com.awslabs.aws.iot.greengrass.cdd.providers.interfaces;

import com.amazonaws.SdkClientException;

public interface SdkErrorHandler {
    void handleSdkError(SdkClientException e);
}
