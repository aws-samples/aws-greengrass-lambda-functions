package com.timmattison.greengrass.cdd.events;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import lombok.Builder;
import lombok.Data;

import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
public class GreengrassLambdaEvent {
    @Builder.Default
    private Optional<String> topic = Optional.empty();
    @Builder.Default
    private Optional<Map> jsonInput = Optional.empty();
    @Builder.Default
    private Optional<byte[]> binaryInput = Optional.empty();
    @Builder.Default
    private Optional<OutputStream> outputStream = Optional.empty();
    private Context context;
    private LambdaLogger logger;

    public Object getInput() {
        if (getJsonInput().isPresent()) {
            return getJsonInput().get();
        }

        if (getBinaryInput().isPresent()) {
            return getBinaryInput().get();
        }

        return null;
    }
}
