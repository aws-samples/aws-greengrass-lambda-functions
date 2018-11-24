package com.timmattison.greengrass.cdd.communication;

import com.amazonaws.greengrass.javasdk.IotDataClient;
import com.amazonaws.greengrass.javasdk.LambdaClient;
import com.amazonaws.greengrass.javasdk.model.*;
import com.google.gson.Gson;
import com.timmattison.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GreengrassCommunication implements Communication {
    public static final String EMPTY_CUSTOM_CONTEXT = "{}";
    public static final String ENCODED_EMPTY_CUSTOM_CONTEXT = Base64.getEncoder().encodeToString(EMPTY_CUSTOM_CONTEXT.getBytes());
    private final IotDataClient iotDataClient;
    private final LambdaClient lambdaClient;
    private final EnvironmentProvider environmentProvider;

    @Override
    public void publish(String topic, Object object) throws GGIotDataException, GGLambdaException {
        PublishRequest publishRequest = new PublishRequest().withTopic(topic).withPayload(ByteBuffer.wrap(new Gson().toJson(object).getBytes()));

        iotDataClient.publish(publishRequest);
    }

    @Override
    public void publish(String topic, byte[] bytes) throws GGIotDataException, GGLambdaException {
        PublishRequest publishRequest = new PublishRequest().withTopic(topic).withPayload(ByteBuffer.wrap(bytes));

        iotDataClient.publish(publishRequest);
    }

    public Optional<byte[]> invokeByName(String functionName, Optional<Map> customContext, byte[] binaryData) {
        Optional<String> functionArn = environmentProvider.getLocalLambdaArn(functionName);

        if (!functionArn.isPresent()) {
            log.error("Attempted to invoke Lambda by name [" + functionName + "] but its ARN is not in the environment");
            return Optional.empty();
        }

        String encodedClientContext = ENCODED_EMPTY_CUSTOM_CONTEXT;

        if (customContext.isPresent()) {
            encodedClientContext = Base64.getEncoder().encodeToString(new Gson().toJson(customContext.get()).getBytes());
        }

        final InvokeRequest invokeRequest = new InvokeRequest()
                .withFunctionArn(functionArn.get())
                .withInvocationType(InvocationType.RequestResponse)
                .withClientContext(encodedClientContext)
                .withPayload(ByteBuffer.wrap(binaryData));

        try {
            final InvokeResponse response;
            response = lambdaClient.invoke(invokeRequest);
            final byte[] bytes = response.getPayload().array();

            return Optional.ofNullable(bytes);
        } catch (GGLambdaException e) {
            log.error("Lambda invoke failed [" + e.getMessage() + "]");
            return Optional.empty();
        }
    }
}
