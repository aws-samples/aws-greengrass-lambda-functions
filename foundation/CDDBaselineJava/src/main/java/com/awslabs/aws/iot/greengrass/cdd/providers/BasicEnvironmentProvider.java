package com.awslabs.aws.iot.greengrass.cdd.providers;

import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;

import java.util.Optional;

/**
 * This class contains variables provided by the CDD wrapper and the provisioner, not the Greengrass environment
 */
public class BasicEnvironmentProvider implements EnvironmentProvider {
    @Override
    public Optional<String> getAwsIotThingArn() {
        return get(AWS_IOT_THING_ARN);
    }

    @Override
    public Optional<String> getGroupId() {
        return get(GROUP_ID);
    }
}
