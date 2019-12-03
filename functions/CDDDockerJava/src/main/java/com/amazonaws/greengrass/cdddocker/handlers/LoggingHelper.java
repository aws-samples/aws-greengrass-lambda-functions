package com.amazonaws.greengrass.cdddocker.handlers;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.util.Optional;

public interface LoggingHelper {
    void logAndPublish(Optional<LambdaLogger> logger, String topic, String message);
}
