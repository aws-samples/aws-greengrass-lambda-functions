package com.amazonaws.greengrass.cdddocker.handlers;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.awslabs.aws.iot.greengrass.cdd.communication.Dispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class BasicLoggingHelper implements LoggingHelper {
    private final Logger log = LoggerFactory.getLogger(BasicLoggingHelper.class);
    @Inject
    Dispatcher dispatcher;

    @Inject
    public BasicLoggingHelper() {
    }

    @Override
    public void logAndPublish(Optional<LambdaLogger> logger, String topic, String message) {
        logger.ifPresent(l -> l.log(message));

        if (!logger.isPresent()) {
            log.info(message);
        }

        dispatcher.publishMessageEvent(topic, message);
    }
}
