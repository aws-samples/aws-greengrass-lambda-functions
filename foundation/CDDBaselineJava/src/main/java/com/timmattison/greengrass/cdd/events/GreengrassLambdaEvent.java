package com.timmattison.greengrass.cdd.events;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.immutables.value.Value;

import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

@Value.Immutable
public abstract class GreengrassLambdaEvent {
    public abstract Optional<String> getTopic();

    public abstract Optional<Map> getJsonInput();

    public abstract Optional<byte[]> getBinaryInput();

    public abstract Optional<OutputStream> getOutputStream();

    public abstract Context getContext();

    public abstract LambdaLogger getLogger();

    Object getInput() {
        if (getJsonInput().isPresent()) {
            return getJsonInput().get();
        }

        if (getBinaryInput().isPresent()) {
            return getBinaryInput().get();
        }

        return null;
    }
}
