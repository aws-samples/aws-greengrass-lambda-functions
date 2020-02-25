package com.awslabs.aws.iot.greengrass.cdd.communication;

import com.amazonaws.greengrass.javasdk.IotDataClient;
import com.amazonaws.greengrass.javasdk.model.*;
import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public class GreengrassCommunication implements Communication {
    private final Logger log = LoggerFactory.getLogger(GreengrassCommunication.class);
    private static final String EMPTY_CUSTOM_CONTEXT = "{}";
    private static final String ENCODED_EMPTY_CUSTOM_CONTEXT = Base64.getEncoder().encodeToString(EMPTY_CUSTOM_CONTEXT.getBytes());
    @Inject
    IotDataClient iotDataClient;
    @Inject
    LambdaClientInterface lambdaClientInterface;
    @Inject
    EnvironmentProvider environmentProvider;

    @Inject
    public GreengrassCommunication(EnvironmentProvider environmentProvider, LambdaClientInterface lambdaClientInterface, IotDataClient iotDataClient) {
        this.environmentProvider = environmentProvider;
        this.lambdaClientInterface = lambdaClientInterface;
        this.iotDataClient = iotDataClient;
    }

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

    @Override
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
            response = lambdaClientInterface.invoke(invokeRequest);
            final byte[] bytes = response.getPayload().array();

            return Optional.ofNullable(bytes);
        } catch (GGLambdaException e) {
            log.error("Lambda invoke failed [" + e.getMessage() + "]");
            return Optional.empty();
        }
    }
}
