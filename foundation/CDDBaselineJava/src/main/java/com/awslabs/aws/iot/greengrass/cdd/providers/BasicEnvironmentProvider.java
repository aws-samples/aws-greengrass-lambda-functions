package com.awslabs.aws.iot.greengrass.cdd.providers;

import com.awslabs.aws.iot.greengrass.cdd.providers.interfaces.EnvironmentProvider;

import javax.inject.Inject;
import java.util.Optional;

/**
 * This class contains variables provided by the CDD wrapper and the provisioner, not the Greengrass environment
 */
public class BasicEnvironmentProvider implements EnvironmentProvider {
    @Inject
    public BasicEnvironmentProvider() {
    }

    @Override
    public Optional<String> getAwsIotThingArn() {
        return get(AWS_IOT_THING_ARN);
    }

    @Override
    public Optional<String> getGroupId() {
        return get(GROUP_ID);
    }
}
