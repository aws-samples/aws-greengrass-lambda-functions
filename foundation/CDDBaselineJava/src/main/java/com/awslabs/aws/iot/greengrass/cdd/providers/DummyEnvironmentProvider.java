package com.awslabs.aws.iot.greengrass.cdd.providers;

import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;

import javax.inject.Inject;
import java.util.Optional;

/**
 * This class contains variables provided by the CDD wrapper and the provisioner, not the Greengrass environment
 */
public class DummyEnvironmentProvider implements EnvironmentProvider {
    @Inject
    public DummyEnvironmentProvider() {
    }

    @Override
    public Optional<String> getAwsIotThingArn() {
        return Optional.of("arn:aws:iot:us-east-1:123456789012:thing/Core");
    }

    @Override
    public Optional<String> getPath() {
        return Optional.of("/usr/bin:/usr/local/bin");
    }

    @Override
    public Optional<String> getAwsIotThingName() {
        return Optional.of("Core");
    }

    @Override
    public Optional<String> getShadowFunctionArn() {
        return Optional.of("arn:aws:lambda:::function:GGShadowService");
    }

    @Override
    public Optional<String> getRouterFunctionArn() {
        return Optional.of("arn:aws:lambda:::function:GGRouter");
    }

    @Override
    public Optional<String> getAwsGgHttpEndpoint() {
        return Optional.of("greengrass-ats.iot.us-east-1.amazonaws.com:8443");
    }

    @Override
    public Optional<String> getAwsIotHttpEndpoint() {
        return Optional.of("axxxxxxxxxxxxy.iot.us-east-1.amazonaws.com:8443");
    }

    @Override
    public Optional<String> getAwsGgMqttEndpoint() {
        return Optional.of("greengrass-ats.iot.us-east-1.amazonaws.com:8883");
    }

    @Override
    public Optional<String> getAwsIotMqttEndpoint() {
        return Optional.of("xxxxxcxxxxxxyd.iot.us-east-1.amazonaws.com:8883");
    }

    @Override
    public Optional<String> getAwsContainerAuthorizationToken() {
        return Optional.of("Basic M0000000000000000000000000000000000000000000000i");
    }

    @Override
    public Optional<String> getGgMqttKeepAlive() {
        return Optional.of("600");
    }

    @Override
    public Optional<String> getMyFunctionArn() {
        return Optional.of("arn:aws:lambda:us-east-1:123456789012:function:CDDEchoJava:44");
    }

    @Override
    public Optional<String> getHome() {
        return Optional.of("/root");
    }

    @Override
    public Optional<String> getAwsContainerCredentialsFullUri() {
        return Optional.of("http://localhost:8000/2016-11-01/credentialprovider/");
    }

    @Override
    public Optional<String> getGroupId() {
        return Optional.of("9aa3c8c0-094f-4165-8bf4-b63f1ee6bb86");
    }
}
