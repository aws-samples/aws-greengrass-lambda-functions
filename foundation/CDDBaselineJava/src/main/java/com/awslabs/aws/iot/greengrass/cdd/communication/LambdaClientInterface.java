package com.awslabs.aws.iot.greengrass.cdd.communication;

import com.amazonaws.greengrass.javasdk.model.GGLambdaException;
import com.amazonaws.greengrass.javasdk.model.InvokeRequest;
import com.amazonaws.greengrass.javasdk.model.InvokeResponse;

public interface LambdaClientInterface {
    InvokeResponse invoke(InvokeRequest invokeRequest) throws GGLambdaException;
}
